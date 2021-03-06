import java.io.File;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

public class IntentUtil {
	/**
	 * 获取打开无线网络设置的Intent
	 *
	 * @return 无线网络设置的Intent
	 */
	public static Intent getWirelessSettingsIntent() {
		return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	}

	/**
	 * 获取短信发送的Intent
	 * @param smsto  短信接收号码
	 * @param sms_body 短信内容
	 * @return 短信发送的Intent
	 */
	public static Intent getSmsIntent( String smsto , String sms_body ) {
		Uri smsToUri = Uri.parse("smsto:" + smsto);
		Intent intent = new Intent(android.content.Intent.ACTION_SENDTO , smsToUri);
		intent.putExtra("sms_body" , sms_body);
		return intent;
	}

	/**
	 * 获取邮件发送的Intent
	 * @param subject 邮件标题
	 * @param body   邮件正文
	 * @return 邮件发送的Intent
	 */
	public static Intent getEmailIntent( String subject , String body ) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra("subject" , subject); // 标题
		intent.putExtra("body" , body); // 正文
		return intent;
	}
	/**
	 * 发送邮件
	 * @param receiver  收件人集合
	 * @return
	 */
	public static Intent getEmailIntent(String[] receiver){
//		String[] receiver = new String[] {text};  
		String subject = "";
		String content = "";
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("message/rfc822");
		// 设置邮件发收人
		email.putExtra(Intent.EXTRA_EMAIL, receiver);
		// 设置邮件标题
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		// 设置邮件内容
		email.putExtra(Intent.EXTRA_TEXT, content);
		// 调用系统的邮件系统
		return email;
	}
	/**
	 * 获取直接拨号的Intent
	 * @param phoneNum 电话号码
	 * @return 直接拨号的Intent
	 * @see 需添加<uses-permission id="android.permission.CALL_PHONE"/>权限
	 */
	public static Intent getPhoneCallIntent( String phoneNum ) {
		Uri uri = Uri.parse("tel:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_CALL , uri);
		return intent;
	}

	/**
	 * 获取跳转到拨号界面的Intent
	 * @param phoneNum 电话号码
	 * @return 跳转到拨号界面的Intent
	 */
	public static Intent getPhoneDialIntent( String phoneNum ) {
		Uri uri = Uri.parse("tel:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_DIAL , uri);
		return intent;
	}

	/**
	 * 获取调用摄像头的Intent
	 * @param photopath  拍摄照片保存的路径(包含文件名)
	 * @return 调用摄像头的Intent
	 */
	public static Intent getCameraIntent( String photopath ) {
		// 选择照相图片
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
		// 获取这个图片的URI
		File photofile = new File(photopath);
		Uri originalUri = Uri.fromFile(photofile);// 这是个实例变量，方便下面获取图片的时候用
		intent.putExtra(MediaStore.EXTRA_OUTPUT , originalUri);
		return intent;
	}

	/**
	 * 获取打开各种常见文件的Intent,包括jpg,gif,png,pdf,txt,mp3,avi,chm,doc,docx,xls,xlsx,ppt ,pptx
	 * @param filePath   文件路径
	 * @return intent 打开各种常见文件的Intent
	 */
	public static Intent getFileIntent( String filePath ) {
		String temp = "";
		if ( !StringUtil.isEmpty(filePath) ) {
			int i = filePath.lastIndexOf(".");
			temp = filePath.substring(i + 1).toLowerCase();
		}
		// 考虑各种格式
		if ( temp.equalsIgnoreCase("jpg") || temp.equalsIgnoreCase("gif") || temp.equalsIgnoreCase("png") ) {
			return getImageFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("pdf") ) {
			return getPdfFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("txt") ) {
			return getTextFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("mp3") ) {
			return getAudioFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("avi") ) {
			return getVideoFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("chm") ) {
			return getChmFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("doc") || temp.equalsIgnoreCase("docx") ) {
			return getWordFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("xls") || temp.equalsIgnoreCase("xlsx") ) {
			return getExcelFileIntent(filePath);
		}

		if ( temp.equalsIgnoreCase("ppt") || temp.equalsIgnoreCase("pptx") ) {
			return getPptFileIntent(filePath);
		}
		if ( temp.equalsIgnoreCase("html") || temp.equalsIgnoreCase("htm") ) {
//			Intent intent= new Intent();        
//		    intent.setAction("android.intent.action.VIEW");    
//		    Uri content_url = Uri.parse(filePath);   
//		    intent.setData(content_url);  

//		    Intent intent= new Intent();        
//            intent.setAction("android.intent.action.VIEW");    
//            Uri content_url = Uri.parse("content://com.android.htmlfileprovider"+filePath);   
//            intent.setData(content_url);           
// 
//			intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity")); 

			return getHtmlFileIntent(filePath);
		}
		return null;
	}
	/**
	 * 打开html 和htm
	 * @param filePah
	 * @return
	 */
	private static Intent getHtmlFileIntent(String param)
	{
		File file = new File(param);

		Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}
	// android获取一个用于打开图片文件的intent
	private static Intent getImageFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "image/*");
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	private static Intent getPdfFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "application/pdf");
		return intent;
	}

	// android获取一个用于打开文本文件的intent
	private static Intent getTextFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "text/plain");
		return intent;
	}

	// android获取一个用于打开音频文件的intent
	private static Intent getAudioFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot" , 0);
		intent.putExtra("configchange" , 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "audio/*");
		return intent;
	}

	// android获取一个用于打开视频文件的intent
	private static Intent getVideoFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot" , 0);
		intent.putExtra("configchange" , 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "video/*");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	private static Intent getChmFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "application/x-chm");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	private static Intent getWordFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "application/msword");
		return intent;
	}

	// android获取一个用于打开Excel文件的intent
	private static Intent getExcelFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "application/vnd.ms-excel");
		return intent;
	}

	// android获取一个用于打开PPT文件的intent
	private static Intent getPptFileIntent( String param ) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri , "application/vnd.ms-powerpoint");
		return intent;
	}
}
