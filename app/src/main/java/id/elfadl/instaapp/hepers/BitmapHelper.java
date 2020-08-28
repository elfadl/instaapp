package id.elfadl.instaapp.hepers;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A helper class to conveniently alter Bitmap data.
 * 
 * @author Ralf Gehrer <ralf@ecotastic.de>
 */
public class BitmapHelper {

	/**
     * Converts a Bitmap to a byteArray.
     * @return byteArray
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Converts a byteArray to a Bitmap object
     * @param byteArray
     * @return Bitmap
     */
    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        } else {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
    }

    /**
     * Shrinks and a passed Bitmap.
     * 
     * @param bm
     * @param maxLengthOfEdge
     * @return Bitmap
     */
    public static Bitmap shrinkBitmap(Bitmap bm, int maxLengthOfEdge) {
        return shrinkBitmap(bm, maxLengthOfEdge, 0);
    }

    /**
     * Shrinks and rotates (if necessary) a passed Bitmap.
     * 
     * @param bm
     * @param maxLengthOfEdge
     * @param rotateXDegree
     * @return Bitmap
     */
    public static Bitmap shrinkBitmap(Bitmap bm, int maxLengthOfEdge, int rotateXDegree) {
        if (maxLengthOfEdge > bm.getWidth() && maxLengthOfEdge > bm.getHeight()) {
            return bm;
        } else {
            // shrink image
            float scale = (float) 1.0;
            if (bm.getHeight() > bm.getWidth()) {
                scale = ((float) maxLengthOfEdge) / bm.getHeight();
            } else {
                scale = ((float) maxLengthOfEdge) / bm.getWidth();
            }
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scale, scale);
            matrix.postRotate(rotateXDegree);

            // RECREATE THE NEW BITMAP
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                    matrix, false);

            matrix = null;
            System.gc();

            return bm;
        }
    }

    /**
     * Reads a Bitmap from an Uri.
     * 
     * @param context
     * @param selectedImage
     * @return Bitmap
     */
    public static Bitmap readBitmap(Context context, Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inScaled = false;
//      options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(
                        fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                return null;
            }
        }
        return bm;
    }

    /**
     * Clears all Bitmap data, that is, recycles the Bitmap and 
     * triggers the garbage collection.
     * 
     * @param bm
     */
    public static void clearBitmap(Bitmap bm) {
        bm.recycle();
        System.gc();
    }	
    
    
    /**
     * Deletes an image given its Uri and refreshes the gallery thumbnails.
     * @param cameraPicUri
     * @param context
     * @return true if it was deleted successfully, false otherwise.
     */
	public static boolean deleteImageWithUriIfExists(Uri cameraPicUri, Context context) {
		try {
			if (cameraPicUri != null) {
				File fdelete = new File(cameraPicUri.getPath());
		        if (fdelete.exists()) {
		            if (fdelete.delete()) {
	            		refreshGalleryImages(context, fdelete);
		            	return true;
		            }
		        }	
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Forces the Android gallery to  refresh its thumbnail images.
	 * @param context
	 * @param fdelete
	 */
	private static void refreshGalleryImages(Context context, File fdelete) {
		try {
    		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
    	} catch (Exception e1) {
    		try {
        		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        	    Uri contentUri = Uri.fromFile(fdelete);
        	    mediaScanIntent.setData(contentUri);
        	    context.sendBroadcast(mediaScanIntent);
    		} catch (Exception e2) {
    		}
    	}		
	}

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation){
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try{
            Bitmap bmRotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            bitmap.recycle();
            return bmRotated;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeFile(String filePath){
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp <= REQUIRED_SIZE && height_tmp <= REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap photo = BitmapFactory.decodeFile(filePath, o2);

        ExifInterface exif = null;
        try{
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif != null ? exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED) : 0;

        //pic.setImageBitmap(rotateBitmap);
        return BitmapHelper.rotateBitmap(photo, orientation);
    }

}