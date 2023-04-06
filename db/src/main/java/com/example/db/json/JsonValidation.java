package com.example.db.json;

import com.example.db.model.Database;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import lombok.SneakyThrows;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;


public class JsonValidation {

    @SneakyThrows
    public boolean schemaValidator(String db_name , String collection_name , String json){
        String path = Database.getInstance().getDB_PATH() + db_name + "/schema/schema_"+collection_name+".json";
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance();
        JsonSchema jsonSchema = jsonSchemaFactory.getSchema(readSchemaFile(path));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Set<ValidationMessage> validationMessageSet =jsonSchema.validate(jsonNode);

        return validationMessageSet.isEmpty();
    }

    public String readSchemaFile(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
