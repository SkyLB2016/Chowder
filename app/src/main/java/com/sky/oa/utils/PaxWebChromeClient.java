package com.sky.oa.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.sky.sdk.utils.LogUtils;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class PaxWebChromeClient extends WebChromeClient {
    private static final String TAG = "PaxWebChromeClient";
    private final static int VIDEO_REQUEST = 0x11;
    private final static int PHOTO_REQUEST = 0x22;
    private Activity mActivity;
    private ValueCallback<Uri> uploadFile;//定义接受返回值
    private ValueCallback<Uri[]> uploadFiles;
    private Uri imageUri;

    public PaxWebChromeClient(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        request.grant(request.getResources());
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.uploadFile = uploadMsg;
        openCamera(acceptType);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
        this.uploadFile = uploadMsgs;
    }

    // For Android  > 4.1.1
//    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        this.uploadFile = uploadMsg;
        openCamera(acceptType);
    }

    // For Android  >= 5.0
    @Override
    @SuppressLint("NewApi")
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        this.uploadFiles = filePathCallback;
        String[] acceptTypes = fileChooserParams.getAcceptTypes();
        for (int i = 0; i < acceptTypes.length; i++) {
            openCamera(acceptTypes[i]);
            LogUtils.e("类型类型类型==" + acceptTypes[i]);
        }
        return true;
    }

    private void openCamera(String accept) {
        if (accept.contains("image")) {//            image/*
//            File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
//            imageUri = Uri.fromFile(fileUri);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                imageUri = FileProvider.getUriForFile(mFragment.getActivity(), "com.mgyb.suning" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
//            }
//            PhotoUtils.takePicture(mFragment.getActivity(), imageUri, PHOTO_REQUEST);
            // 指定拍照存储位置的方式调起相机
            String fileName =
                    mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //第二参数是在manifest.xml定义 provider的authorities属性
                imageUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".fileprovider", file);
                //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(intent, PHOTO_REQUEST);
        } else if (accept.contains("video")) {//     video/*
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            // set the video file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
            //限制时长
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
            //开启摄像机
            mActivity.startActivityForResult(intent, VIDEO_REQUEST);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VIDEO_REQUEST:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (null != uploadFiles) {
                        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                case PHOTO_REQUEST:
                    if (null == uploadFile && null == uploadFiles) return;
                    Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                    if (uploadFiles != null) {
                        onActivityResultAboveL(requestCode, resultCode, data);
                    } else if (uploadFile != null) {
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != PHOTO_REQUEST || uploadFiles == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadFiles.onReceiveValue(results);
        uploadFiles = null;
    }
}
