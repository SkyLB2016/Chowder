package com.sky.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import com.sky.R;
import com.sky.utils.pending.BitUtils;

import java.io.File;

/**
 * Created by SKY on 2017/8/18.
 * 照片选择器
 */
public class PhotoUtils {
    private AppCompatActivity activity;
    private String photoName;

    private static final int PHOTO = 1501; // 拍照
    private static final int PHOTO_PERMISSIONS = 1502; // 拍照权限请求
    private static final int LOCAL_PHOTO = 1503; // 图库
    private static final int LOCAL_PHOTO_PERMISSIONS = 1504; // 图库相册权限

    @SuppressLint("RestrictedApi")
    public PhotoUtils(AppCompatActivity context, String photoName) {
        this.activity = context;
        this.photoName = photoName;
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialog))
                .setItems(new String[]{"拍照", "本地照片"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) checkCamera();//拍照
                                else checkAlbum();//本地图库
                            }
                        })
                .show();
    }

    //打开相机
    private void checkCamera() {
        //检测是否有相机和读写文件权限
        if (AppUtils.isPermission(activity, Manifest.permission.CAMERA)) startCamera();
        else AppUtils.requestPermission(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                PHOTO_PERMISSIONS);
    }

    //打开本地图库
    private void checkAlbum() {
        if (AppUtils.isPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            openAlbum();
        else AppUtils.requestPermission(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                LOCAL_PHOTO_PERMISSIONS);
    }

    //打开相机
    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(photoName);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //第二参数是在manifest.xml定义 provider的authorities属性
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, PHOTO);
    }

    //打开相册
    private void openAlbum() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        activity.startActivityForResult(galleryIntent, LOCAL_PHOTO);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCAL_PHOTO_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openAlbum();
                else showToast("选择相册需要读写文件权限");
                break;
            case PHOTO_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startCamera();
                else showToast("拍照功能需要相机和读写文件权限");
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        String error = activity.getString(R.string.photo_fail);
        if (TextUtil.notNull(photoName, error)) return;
        Bitmap bitmap = null;
        switch (requestCode) {
            case PHOTO: // 拍照
                bitmap = BitUtils.loadBitmap(activity, photoName);
                if (TextUtil.notNullObj(bitmap, error)) return;
                BitUtils.saveBitmap(photoName, bitmap);
                break;
            case LOCAL_PHOTO: // 图库选择
                if (data == null) return;
                Uri uri = data.getData(); // 获得图片的uri
                if (TextUtil.notNullObj(uri, error)) return;
                String imgPath = BitUtils.getRealPathFromURI(activity, uri);
                if (TextUtil.notNull(imgPath, error)) return;
                bitmap = BitUtils.loadBitmap(activity, imgPath);
                if (TextUtil.notNullObj(bitmap, error)) return;
                BitUtils.saveBitmap(photoName, bitmap);
                break;
        }
        uploadPicture.UpLoadPicture(photoName, bitmap);
    }

    private void showToast(String text) {
        ToastUtils.showShort(activity, text);
    }

    private UploadPictureListener uploadPicture;

    public void setUploadPicture(UploadPictureListener uploadPicture) {
        this.uploadPicture = uploadPicture;
    }

    public interface UploadPictureListener {
        void UpLoadPicture(String photo, Bitmap bitmap);
    }
}
