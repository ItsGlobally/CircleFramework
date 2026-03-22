package top.itsglobally.CircleFramework.sql.serializer;

import com.google.gson.JsonObject;

public interface Serializable<T> {
    JsonObject serialize();
    T deserialize(JsonObject jsonObject);
}
