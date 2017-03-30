package com.sky.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sky on 16/5/10 下午3:50.
 * Toast统一管理类
 */
public class ToastUtils {

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;//是否提示

    //短时间显示Toast
    public static void showShort(Context context, CharSequence message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //短时间显示Toast
    public static void showShort(Context context, int message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, CharSequence message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void showLong(Context context, int message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  消息
     * @param duration 时间
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  context
     * @param message  消息
     * @param duration 时间
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) Toast.makeText(context, message, duration).show();
    }

    public static void showError(Context context, int code) {
        String errorMsg = "";
        switch (code) {
//            case 200://正常；请求已完成。
//            case 201://正常；紧接 POST 命令。
//            case 202://正常；已接受用于处理，但处理尚未完成。
//            case 203://正常；部分信息 — 返回的信息只是一部分。
//            case 204://正常；无响应 — 已接收请求，但不存在要回送的信息。
//                break;
//            //3xx  重定向
//            case 301://已移动 — 请求的数据具有新的位置且更改是永久的。
//            case 302://已找到 — 请求的数据临时具有不同 URI。
//            case 303://请参阅其它 — 可在另一 URI 下找到对请求的响应，且应使用 GET 方法检索此响应。
//            case 304://未修改 — 未按预期修改文档。
//            case 305://使用代理 — 必须通过位置字段中提供的代理来访问请求的资源。
//            case 306://未使用 — 不再使用；保留此代码以便将来使用。
//                break;
//            //4xx  客户机中出现的错误
//            case 400://错误请求 — 请求中有语法问题，或不能满足请求。
//                break;
//            case 401://未授权 — 未授权客户机访问数据。
//                break;
//            case 402://需要付款 — 表示计费系统已有效。
//                break;
//            case 403://禁止 — 即使有授权也不需要访问。
//                break;
//            case 404://找不到 — 服务器找不到给定的资源；文档不存在。
//                errorMsg = "服务器找不到给定的资源";
//            case 407://代理认证请求 — 客户机首先必须使用代理认证自身。
//                break;
//            case 415://介质类型不受支持 — 服务器拒绝服务请求，因为不支持请求实体的格式。
//                break;
//            //5xx  服务器中出现的错误
//            case 500://内部错误 — 因为意外情况，服务器不能完成请求。
//            case 501://未执行 — 服务器不支持请求的工具。
//            case 502://错误网关 — 服务器接收到来自上游服务器的无效响应。
//            case 503://无法获得服务 — 由于临时过载或维护，服务器无法处理请求。
//                break;

            case 404://找不到 — 服务器找不到给定的资源；文档不存在。
                errorMsg = "服务器找不到给定的资源";
                break;
            default:
                int codes = code / 100;
                if (codes == 1 || codes == 2 || codes == 3 || codes == 5) {
                    return;
                } else if (codes == 4) {
                    // 4xx系列http错误，需要处理
                    errorMsg = "请先登录";
                }

                break;
        }
        if (errorMsg.length() == 0) return;

        showShort(context, errorMsg);
    }

    public static void showWithCustomView(Context context, int contentLayoutId, int textViewId,
                                          String msg) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflater.inflate(contentLayoutId, null);
        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView tv = (TextView) toastRoot.findViewById(textViewId);
        tv.setText(msg);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}