package com.shangame.fiction.ui.share;

import android.content.Context;
import android.graphics.Bitmap;

import com.shangame.fiction.wxapi.WeChatConstants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Create by Speedy on 2018/9/28
 */
public class WeChatSharer {

    /**
     * 分享至微信聊天
     *
     * @param context
     * @param title
     * @param linkUrl
     * @param thumbBmp
     * @param description
     */
    public static void shareToWeChat(final Context context, String title, String linkUrl, Bitmap thumbBmp, String description) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = linkUrl;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;

        wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);
        if (api != null) {
            api.sendReq(req);
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 75, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享至朋友圈
     *
     * @param context
     * @param title
     * @param linkUrl
     * @param thumbBmp
     * @param description
     */
    public static void shareToFriendCircle(final Context context, String title, String linkUrl, Bitmap thumbBmp, String description) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = linkUrl;

        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;

        wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);
        if (api != null) {
            api.sendReq(req);
        }
    }

    public static void shareImageToWeChat(final Context context, final String imgPath) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(imgPath);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
//        req.openId = WeChatConstants.OPEN_ID.SHARE_IMAGE_TO_WECHAT;
        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);

        if (api != null) {
            api.sendReq(req);
        }
    }

    public static void shareImageToFriendCircle(final Context context, final String imgPath) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(imgPath);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);
        if (api != null) {
            api.sendReq(req);
        }
    }

    public static void shareImageToWeChat(final Context context, final Bitmap bitmap) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);
        if (api != null) {
            api.sendReq(req);
        }
    }

    public static void shareImageToFriendCircle(final Context context, final Bitmap bitmap) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        IWXAPI api = WXAPIFactory.createWXAPI(context, WeChatConstants.APP_ID);
        if (api != null) {
            api.sendReq(req);
        }
    }
}
