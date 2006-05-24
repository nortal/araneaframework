package org.araneaframework.servlet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.iharder.base64.Base64;

/**
 * Provides base64 encoding, decoding methods, generating a digest and checking the digest methods.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class EncodingUtil {
	/**
	 * The method serializes the object, optionally compresses it and base64 encodes. 
	 * GZip is used for the optional compression.
	 * 
	 * The method uses streams for the different operations and the underlying base64
	 * algorithm is from iHarder.net base64 utilities..
	 * 
	 * @param object that will be encoded.
	 * @param compress if set, the serialized object will be compressed.
	 * @return A String represtation of the object in base64 encoded format.
	 * @throws Exception
	 */
	public static String encodeObjectBase64(Serializable object, boolean compress) throws Exception {
		ByteArrayOutputStream baos = null;
		Base64.OutputStream b64os = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			b64os = new Base64.OutputStream(baos, Base64.ENCODE | Base64.DONT_BREAK_LINES);
			OutputStream os = compress ? ((OutputStream) new GZIPOutputStream(b64os)) : b64os;
			oos = new ObjectOutputStream(os);
	
			oos.writeObject(object);
			
			oos.flush();
		}
		finally {
			oos.close();
		}
	
		return new String(baos.toByteArray(), "UTF-8");
	}

	/**
	 * Base64 decodes the value, optionally decompresses it and then deserializes it. For
	 * the decompression GZip is expected.
	 * 
	 * @param value is the base64 encoded data of the optionally compressed serialized object. 
	 * @param compress determines if after the decoding gzip decompression should be applied.
	 * @return an Object that is deserialized object of the decoded optionally decompressed value. 
	 * @throws Exception
	 */
	public static Object decodeObjectBase64(String value, boolean compress) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(value.getBytes("UTF-8"));
		Base64.InputStream b64is = new Base64.InputStream(bais, Base64.DECODE | Base64.DONT_BREAK_LINES);
		InputStream is = compress ? ((InputStream) new GZIPInputStream(b64is)) : b64is;
		ObjectInputStream ois = new ObjectInputStream(is);
		
		return ois.readObject();
	}

	/**
	 * Builds a digest of the byte array, using the SHA hash function.
	 * 
	 * @param data the byte array that's digest we are building.
	 * @return A byte[] array representing the SHA hash.
	 * @throws Exception
	 */
	public static byte[] buildDigest(byte[] data) throws Exception {
		MessageDigest dgst = MessageDigest.getInstance("SHA");
		dgst.update(data);		
		return dgst.digest();
	}

	/**
	 * Returns true, if the digest of the value equals to the given digest.
	 * 
	 * @param value byte array that's digest we are comparing. 
	 * @param digest the allready generated digest we are comparing against.
	 * @return true if the digest of the value equals digest, otherwise false.
	 * @throws Exception
	 */
	public static boolean checkDigest(byte[] value, byte[] digest) throws Exception {
		return MessageDigest.isEqual(buildDigest(value), digest);
	}

}
