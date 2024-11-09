package com.github.kingschan1204.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

public class JsonTest {

    @Test
    public void schemaTest() throws Exception {
        String jsonString = """
                {
                    "data": [
                        {
                            "zrxh": 3,
                            "jybz": "0",
                            "jyrq": "2024-10-01"
                        },
                        {
                            "zrxh": 4,
                            "jybz": "0",
                            "jyrq": "2024-10-02"
                        },
                        {
                            "zrxh": 5,
                            "jybz": "0",
                            "jyrq": "2024-10-03"
                        },
                        {
                            "zrxh": 6,
                            "jybz": "0",
                            "jyrq": "2024-10-04"
                        },
                        {
                            "zrxh": 7,
                            "jybz": "0",
                            "jyrq": "2024-10-05"
                        },
                        {
                            "zrxh": 1,
                            "jybz": "0",
                            "jyrq": "2024-10-06"
                        },
                        {
                            "zrxh": 2,
                            "jybz": "0",
                            "jyrq": "2024-10-07"
                        },
                        {
                            "zrxh": 3,
                            "jybz": "1",
                            "jyrq": "2024-10-08"
                        },
                        {
                            "zrxh": 4,
                            "jybz": "1",
                            "jyrq": "2024-10-09"
                        },
                        {
                            "zrxh": 5,
                            "jybz": "1",
                            "jyrq": "2024-10-10"
                        },
                        {
                            "zrxh": 6,
                            "jybz": "1",
                            "jyrq": "2024-10-11"
                        },
                        {
                            "zrxh": 7,
                            "jybz": "0",
                            "jyrq": "2024-10-12"
                        },
                        {
                            "zrxh": 1,
                            "jybz": "0",
                            "jyrq": "2024-10-13"
                        },
                        {
                            "zrxh": 2,
                            "jybz": "1",
                            "jyrq": "2024-10-14"
                        },
                        {
                            "zrxh": 3,
                            "jybz": "1",
                            "jyrq": "2024-10-15"
                        },
                        {
                            "zrxh": 4,
                            "jybz": "1",
                            "jyrq": "2024-10-16"
                        },
                        {
                            "zrxh": 5,
                            "jybz": "1",
                            "jyrq": "2024-10-17"
                        },
                        {
                            "zrxh": 6,
                            "jybz": "1",
                            "jyrq": "2024-10-18"
                        },
                        {
                            "zrxh": 7,
                            "jybz": "0",
                            "jyrq": "2024-10-19"
                        },
                        {
                            "zrxh": 1,
                            "jybz": "0",
                            "jyrq": "2024-10-20"
                        },
                        {
                            "zrxh": 2,
                            "jybz": "1",
                            "jyrq": "2024-10-21"
                        },
                        {
                            "zrxh": 3,
                            "jybz": "1",
                            "jyrq": "2024-10-22"
                        },
                        {
                            "zrxh": 4,
                            "jybz": "1",
                            "jyrq": "2024-10-23"
                        },
                        {
                            "zrxh": 5,
                            "jybz": "1",
                            "jyrq": "2024-10-24"
                        },
                        {
                            "zrxh": 6,
                            "jybz": "1",
                            "jyrq": "2024-10-25"
                        },
                        {
                            "zrxh": 7,
                            "jybz": "0",
                            "jyrq": "2024-10-26"
                        },
                        {
                            "zrxh": 1,
                            "jybz": "0",
                            "jyrq": "2024-10-27"
                        },
                        {
                            "zrxh": 2,
                            "jybz": "1",
                            "jyrq": "2024-10-28"
                        },
                        {
                            "zrxh": 3,
                            "jybz": "1",
                            "jyrq": "2024-10-29"
                        },
                        {
                            "zrxh": 4,
                            "jybz": "1",
                            "jyrq": "2024-10-30"
                        },
                        {
                            "zrxh": 5,
                            "jybz": "1",
                            "jyrq": "2024-10-31"
                        }
                    ],
                    "nowdate": "2024-11-09"
                }
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // 递归解析 JSON，生成 schema
        JsonNode schema = generateSchema(rootNode);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
    }

    // 递归方法来生成 JSON schema
    public static JsonNode generateSchema(JsonNode node) {

        ObjectMapper objectMapper = new ObjectMapper();
        if (node.isObject()) {
            // 如果是对象，遍历其所有字段
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            JsonNode schemaNode = objectMapper.createObjectNode();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                ((ObjectNode) schemaNode).set(field.getKey(), generateSchema(field.getValue()));
            }

            return schemaNode;
        } else if (node.isArray()) {
            // 如果是数组，检查数组中的元素类型
            JsonNode schemaNode = objectMapper.createObjectNode();
            ((ObjectNode) schemaNode).put("type", "array");
            ((ObjectNode) schemaNode).set("items", generateSchema(node.get(0)));

            return schemaNode;
        } else if (node.isTextual()) {
            return objectMapper.createObjectNode().put("type", "string");
        } else if (node.isInt()) {
            return objectMapper.createObjectNode().put("type", "integer");
        } else if (node.isBoolean()) {
            return objectMapper.createObjectNode().put("type", "boolean");
        } else if (node.isNull()) {
            return objectMapper.createObjectNode().put("type", "null");
        } else {
            return objectMapper.createObjectNode().put("type", "unknown");
        }
    }

}
