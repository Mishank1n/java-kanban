package work.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import work.types.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(Task.insertFormatter));
        } else {
            jsonWriter.value("");
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        if (!string.equals("")) {
            return LocalDateTime.parse(string, Task.insertFormatter);
        } else {
            return null;
        }
    }
}
