package com.integritylabsbrazil.tools.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.MapFunction;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Predicate;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    private static final DeserializationAwareObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static JsonNode filterIgnorableFlattenedField(JsonNode flattenedNode) {
        Iterator<Map.Entry<String, JsonNode>> it = flattenedNode.fields();

        while (it.hasNext()) {
            Map.Entry<String, JsonNode> child = (Map.Entry) it.next();
            if (((JsonNode) child.getValue()).isNull() || ((JsonNode) child.getValue()).isObject() || ((JsonNode) child.getValue()).isArray()) {
                it.remove();
            }
        }

        return flattenedNode;
    }

    private static final DeserializationAwareObjectMapper createObjectMapper() {
        DeserializationAwareObjectMapper objectMapper = new DeserializationAwareObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    public static <T> T getByJsonPath(JsonNode node, String path) {
        try {
            return (T) normalize(JsonPath.read(node.toString(), path, new Predicate[0]));
        } catch (PathNotFoundException var3) {
            return null;
        }
    }

    public static JsonNode mapByJsonPath(JsonNode node, String path, Function<Object, Object> mapper) {
        try {
            DocumentContext context = JsonPath.parse(node.toString());
            context.map(path, mapFunctionFor(mapper), new Predicate[0]);
            return (JsonNode) readValue(context.jsonString(), JsonNode.class);
        } catch (PathNotFoundException var4) {
            return node;
        }
    }

    private static MapFunction mapFunctionFor(Function<Object, Object> mapper) {
        return (value, configuration) -> mapper.apply(normalize(value));
    }

    public static <T extends Number> T getByJsonPath(JsonNode node, String path, Class<T> klass) {
        return (T) (Optional.ofNullable(getByJsonPath(node, path)).map(Object::toString).map((v) -> createNumberFromString(v, klass)).orElse(null));
    }

    private static <T> T normalize(T value) {
        return (T) (value instanceof Double ? new BigDecimal(value.toString()) : value);
    }

    public static JsonNode merge(JsonNode o1, JsonNode o2) {
        if (o1 == null && o2 == null) {
            return null;
        } else if (o1 == null) {
            return (JsonNode) clone(o2);
        } else {
            return o2 == null ? (JsonNode) clone(o1) : doMerge(o1, o2);
        }
    }

    public static ObjectNode emptyJson() {
        return (ObjectNode) fromString("{}");
    }

    public static <T extends JsonNode> T fromString(String json) {
        try {
            return (T) OBJECT_MAPPER.readTree(json);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static String writeValueAsString(Object value) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(value);
            json = json.equals("\"\"") ? "{}" : json;
            return json;
        } catch (JsonProcessingException e) {
            LOG.error("Erro ao gerar JSON: {}", value, e);
            return "";
        }
    }

    public static <T> T readValue(String json, Class<T> valueType) {
        try {
            return (T) (JsonNode.class.isAssignableFrom(valueType) ? OBJECT_MAPPER.readTree(json) : OBJECT_MAPPER.readValue(json, valueType));
        } catch (Exception e) {
            LOG.error("Erro ao gerar objeto a partir do JSON: {}", json, e);
            return null;
        }
    }

    public static <T> T readValue(JsonNode json, Class<T> valueType) {
        try {
            return (T) OBJECT_MAPPER.treeToValue(json, valueType);
        } catch (Exception e) {
            LOG.error("Erro ao gerar objeto a partir do JSON: {}", writeValueAsString(json), e);
            return null;
        }
    }

    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        try {
            return (T) OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (Exception e) {
            LOG.error("Erro ao gerar objeto a partir do JSON: {}", json, e);
            return null;
        }
    }

    public static <T> T clone(T source) {
        if (source == null) {
            return null;
        } else if (source instanceof JsonNode) {
            JsonNode node = (JsonNode) source;
            return (T) node.deepCopy();
        } else {
            return (T) readValue(writeValueAsString(source), source.getClass());
        }
    }

    private static boolean fullSerializationSupported(Class klass) {
        return OBJECT_MAPPER.canSerialize(klass) && OBJECT_MAPPER.canDeserialize(klass);
    }

    public static boolean isJsonAware(Class klass) {
        return fullSerializationSupported(klass) || JsonNode.class.isAssignableFrom(klass);
    }

    private static <T extends Number> T createNumberFromString(String value, Class<T> number) {
        try {
            return (T) (number.getConstructor(String.class).newInstance(value));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> JsonNode flatten(T value) {
        return value == null ? null : filterIgnorableFlattenedField((JsonNode) readValue(JsonFlattener.flatten(writeValueAsString(value)), JsonNode.class));
    }

    public static Map<String, String> asMap(JsonNode node) {
        return node == null ? null : (Map) readValue(writeValueAsString(node), Map.class);
    }

    public static <T> T unflatten(JsonNode data, Class<? extends T> target) {
        return (T) (data == null ? null : readValue(JsonUnflattener.unflatten(writeValueAsString(data)), target));
    }

    public static <T> JsonNode asJsonNode(T value) {
        return value == null ? null : OBJECT_MAPPER.valueToTree(value);
    }

    private static JsonNode doMerge(JsonNode o1, JsonNode o2) {
        try {
            JsonNode result = (JsonNode) clone(o1);
            return (JsonNode) OBJECT_MAPPER.readerForUpdating(result).readValue(o2);
        } catch (Exception ex) {
            LOG.error("Error while trying to merge two JSON objects [o1: {}, o2: {}]", new Object[]{o1, o2, ex});
            throw new IllegalArgumentException(ex);
        }
    }

    public static class DeserializationAwareObjectMapper extends ObjectMapper {

        private static final long serialVersionUID = 7462367195055900604L;

        public boolean canDeserialize(Class klass) {
            try {
                DefaultDeserializationContext context = this.createDeserializationContext((JsonParser) null, this.getDeserializationConfig());
                JsonDeserializer deserializer = context.findRootValueDeserializer(this.constructType(klass));
                if (deserializer instanceof BeanDeserializer beanDeserializer) {
                    return beanDeserializer.getEmptyValue(context) != null;
                } else {
                    return true;
                }
            } catch (JsonMappingException ex) {
                JsonUtil.LOG.trace("Exception raised when checking if {} is deserializable. Considering it to be not deserializable", klass.getName(), ex);
                return false;
            }
        }
    }
}
