package id.elfadl.instaapp.hepers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static id.elfadl.instaapp.hepers.BitmapHelper.decodeFile;

public class UploadHelper {

    public static final String MULTIPART_FORM_DATA = "image/jpg";

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //File file = FilePath.getFile(getContext(), fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        /*RequestBody requestFile =
                RequestBody.create(MediaType.parse(context.getContentResolver().getType(Uri.fromFile(file))), file);*/

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, System.currentTimeMillis() + file.getName(), requestFile);
    }

    public static File fileUpload(Context context, String path) {
        File f = new File(path);
        File file = null;
        try {
            file = new File(context.getCacheDir(), f.getName());
            file.createNewFile();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = decodeFile(f.getPath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            ;
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static String getPath(Context context, Uri uri) {
        if (uri.toString().contains("content:")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            cursor.close();
            return s;
        } else if (uri.toString().contains("file:")) {
            return uri.getPath();
        } else {
            return null;
        }
    }

    public static RequestBody createPartFromString(String param) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), param);
    }


}
