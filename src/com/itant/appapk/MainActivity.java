package com.itant.appapk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView tv_list;
	
	private BufferedInputStream reader;
	private BufferedOutputStream writer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_list = (TextView) findViewById(R.id.tv_list);
		PackageManager pm = getPackageManager();
		StringBuilder sb = new StringBuilder();
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : applicationInfos) {
			sb.append(applicationInfo.publicSourceDir).append("\n");
		}
		tv_list.setText(sb.toString());
		
		String sinaVideoDir = applicationInfos.get(0).publicSourceDir;
		File targetFile = new File(sinaVideoDir);
		
		if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED) && Environment.isExternalStorageEmulated()) {
			try {
				String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "apks" + File.separator;
				File destFileDir = new File(destPath);
				if (!destFileDir.exists()) {
					destFileDir.mkdirs();
				}
				
				File destFile = new File(destPath + "kugou.apk");
				reader = new BufferedInputStream(new FileInputStream(targetFile));
				
				writer = new BufferedOutputStream(new FileOutputStream(destFile));
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, len);
				}
				
				Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "复制失败", Toast.LENGTH_SHORT).show();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			Toast.makeText(this, "无sd卡", Toast.LENGTH_SHORT).show();
		}
	}

}
