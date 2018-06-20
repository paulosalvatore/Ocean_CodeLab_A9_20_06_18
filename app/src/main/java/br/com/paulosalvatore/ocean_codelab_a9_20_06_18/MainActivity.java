package br.com.paulosalvatore.ocean_codelab_a9_20_06_18;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

	private ImageView ivImagem;

	private final String urlImagem = "https://img.global.news.samsung.com/br/wp-content/uploads/2016/08/Samsung-14.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ivImagem = findViewById(R.id.ivImagem);
	}

	public void workerThread(View view) {
		Toast.makeText(this, "onClick WorkerThread", Toast.LENGTH_LONG).show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ivImagem.post(new Runnable() {
					@Override
					public void run() {
						ivImagem.setImageResource(android.R.color.transparent);
					}
				});

				final Bitmap bitmap = carregarImagem(urlImagem);

				ivImagem.post(new Runnable() {
					@Override
					public void run() {
						ivImagem.setImageBitmap(bitmap);
					}
				});
			}
		}).start();
	}

	private Bitmap carregarImagem(String urlImagem) {
		try {
			URL url = new URL(urlImagem);
			Bitmap bitmap = BitmapFactory.decodeStream(
					url.openConnection().getInputStream()
			);
			return bitmap;
		}
		catch (Exception e)
		{
			Log.d("IMAGEM", e.toString());

			return null;
		}
	}

	public void asyncTask(View view) {
		new TarefaAssincrona().execute();
	}

	public void abrirDrawer(View view) {
		Intent i = new Intent(this, DrawerActivity.class);
		startActivity(i);
	}

	public class TarefaAssincrona extends AsyncTask<Void, Void, Bitmap> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					MainActivity.this,
					"Carregando",
					"Carregando imagem..."
			);

			ivImagem.setImageResource(android.R.color.transparent);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			dialog.dismiss();
			ivImagem.setImageBitmap(bitmap);
		}

		@Override
		protected Bitmap doInBackground(Void... voids) {
			return carregarImagem(urlImagem);
		}
	}
}
