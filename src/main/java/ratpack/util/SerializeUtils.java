package ratpack.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
	public static byte[] serialize(Object obj) throws IOException {
		byte[] bytes = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		;
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		bytes = baos.toByteArray();
		baos.close();
		oos.close();

		return bytes;
	}

	public static Object deSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
		Object obj = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		obj = ois.readObject();

		return obj;
	}
}