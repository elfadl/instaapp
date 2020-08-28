package id.elfadl.instaapp.hepers.cache;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.hepers.prefs.Prefs;

public class CacheObject {

    private String key, value;
    private Calendar expiry;

    public CacheObject(String key, String value) {
        try {
            byte[] encrypt = encryptMsg(value);
            this.key = key;
            this.value = new Gson().toJson(encrypt);
            expiry = Calendar.getInstance();
            expiry.add(Calendar.HOUR, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public CacheObject(String key, String value, boolean noExpiry) {
        try {
            byte[] encrypt = encryptMsg(value);
            this.key = key;
            this.value = new Gson().toJson(encrypt);
            if (noExpiry)
                expiry = null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public CacheObject(String key, String value, int milis) {
        try {
            byte[] encrypt = encryptMsg(value);
            this.key = key;
            this.value = new Gson().toJson(encrypt);
            expiry = Calendar.getInstance();
            expiry.add(Calendar.MILLISECOND, milis);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public CacheObject(String key, String value, int field, int amount) {
        try {
            byte[] encrypt = encryptMsg(value);
            this.key = key;
            this.value = new Gson().toJson(encrypt);
            expiry = Calendar.getInstance();
            expiry.add(field, amount);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Nullable
    public String getValue() {
        if (value != null) {
            try {
                Type byteType = new TypeToken<byte[]>() {
                }.getType();
                byte[] text = new Gson().fromJson(value, byteType);
                if (expiry != null) {
                    if (MainApplication.DEBUG_MODE)
                        Log.i("EXPIRY", "getValue: " + new Gson().toJson(expiry) + " NOW : " + new Gson().toJson(new Date()));
                    if (expiry.getTime().before(new Date())) {
                        Prefs.remove(key);
                        return null;
                    }
                }
                return decryptMsg(text);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Calendar getExpiry() {
        return expiry;
    }

    public void setExpiry(Calendar expiry) {
        this.expiry = expiry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(MainApplication.password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey());
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, generateKey());
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
}
