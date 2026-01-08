package app.fitsync.global.config.jpa;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class JsonMapConverter implements AttributeConverter<Map<Object, String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Object, String> attribute) {
        if (attribute == null) {return null;}

        try {
            return mapper.writeValueAsString(attribute);
        } catch (JacksonException e) {
            throw new RuntimeException("json 파싱 중 에러", e);
        }
    }

    @Override
    public Map<Object, String> convertToEntityAttribute(String dbData) {

        if (dbData == null || dbData.isEmpty()) {
            return new HashMap<>();
        }

        try {
            return mapper.readValue(dbData, Map.class);
        } catch (JacksonException e) {
            throw new RuntimeException("json 파싱 중 에러", e);
        }
    }
}
