package pros.app.com.pros.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    public static <T> T from(String data, Class<T> cls) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, cls);
    }

    public static <T> T toList(String data, TypeReference<T> typeReference) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, typeReference);
    }

    public static <E> String fromList(List<E> list) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }

    public static <E> String from(E e) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(e);
    }
}

