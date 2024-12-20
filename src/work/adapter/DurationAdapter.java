package work.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration != null) {
            jsonWriter.value(String.valueOf(duration.toMinutes()));
        } else {
            jsonWriter.value("");
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String minutes = jsonReader.nextString();
        if (!minutes.equals("")) {
            return Duration.ofMinutes(Integer.parseInt(minutes));
        } else {
            return null;
        }
    }
}
