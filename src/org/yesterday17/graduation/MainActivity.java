package org.yesterday17.graduation;

import android.support.v7.app.ActionBarActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Notification n;
	private NotificationManager nm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ///////�����Ƿ��һ������/////////
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			//Toast toast = Toast.makeText(this, "1", Toast.LENGTH_LONG);
			//toast.show();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		} else {
			//Toast toast = Toast.makeText(this, "0", Toast.LENGTH_LONG);
			//toast.show();
		}
		////////////////////////////////////

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		n = new Notification();
		// ����֪ͨ��icon
		n.icon = R.drawable.ic_launcher;
		// ����֪ͨ��״̬������ʾ�Ĺ�����Ϣ
		n.tickerText = "һ��֪ͨ";
		// ����֪ͨ��ʱ��
		n.when = System.currentTimeMillis();

		// /��ӿ�ݷ�ʽ///
		if (!isAddShortCut()) {
			addShortCut();
			Toast toast = Toast.makeText(this, R.string.shortcut_created,
					Toast.LENGTH_LONG);
			toast.show();
		}
		// ///////////////
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isAddShortCut() {

		boolean isInstallShortcut = false;
		final ContentResolver cr = this.getContentResolver();

		int versionLevel = android.os.Build.VERSION.SDK_INT;
		String AUTHORITY = "com.android.launcher2.settings";

		// 2.2���ϵ�ϵͳ���ļ��ļ������ǲ�һ����
		if (versionLevel >= 8) {
			AUTHORITY = "com.android.launcher2.settings";
		} else {
			AUTHORITY = "com.android.launcher.settings";
		}

		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);

		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	public void addShortCut() {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// ��������
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getResources().getString(R.string.app_name));
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this.getApplicationContext(), R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconRes);

		// �Ƿ������ظ�����
		shortcut.putExtra("duplicate", false);

		// ���������ݷ�ʽ��ͼ��
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// �����ݷ�ʽ�Ĳ���
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// intent.setClass(AddShortCutActivity.this, AddShortCutActivity.class);

		// ������������
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// �㲥֪ͨ����ȥ����
		this.sendBroadcast(shortcut);
	}
}
