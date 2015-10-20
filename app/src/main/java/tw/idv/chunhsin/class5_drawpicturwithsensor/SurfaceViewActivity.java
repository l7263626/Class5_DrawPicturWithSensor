package tw.idv.chunhsin.class5_drawpicturwithsensor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    SurfaceHolder holder;
    SensorManager sensorManager;
    int surfaceWidth,surfaceHeight;
    Bitmap bmpCompass,bmpPlate,bmpWidth,bmpHeight;
    Paint paint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        findviews();
        //initBmp();
        initBmp2();
    }

    void initPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    void initBmp(){
        initPaint();
        bmpCompass= BitmapFactory.decodeResource(getResources(), R.drawable.compass);

    }

    void initBmp2(){
        initPaint();
        bmpPlate= BitmapFactory.decodeResource(getResources(), R.drawable.androidplate);
        bmpWidth= BitmapFactory.decodeResource(getResources(), R.drawable.androidwidth);
        bmpHeight= BitmapFactory.decodeResource(getResources(), R.drawable.androidheight);
    }

    void findviews(){
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder,  int format, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        int delay=SensorManager.SENSOR_DELAY_UI;
        sensorManager.registerListener(sensorEventListener, sensor, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    boolean isFirst=true;
    float initDegree=0.0f;

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            if(isFirst){
                initDegree = values[0];
                isFirst=false;
            }
            showDegrees(values);
            //showCompass(values);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    void showCompass(float[] values){

        Canvas canvas = holder.lockCanvas();
        //Bitmap bmp=Bitmap.createBitmap(surfaceWidth, surfaceHeight, Bitmap.Config.ARGB_8888);
        //canvas.setBitmap(bmp);

        //Bitmap bmpCompass= BitmapFactory.decodeResource(getResources(), R.drawable.compass);
        canvas.drawColor(Color.WHITE);
        canvas.save();
        canvas.rotate(-values[0] - 8, surfaceWidth / 2, surfaceHeight / 2);
        canvas.drawBitmap(bmpCompass, surfaceWidth / 2 - bmpCompass.getWidth() / 2, surfaceHeight / 2 - bmpCompass.getHeight() / 2, paint);
        canvas.restore();
        holder.unlockCanvasAndPost(canvas);



    }

    void showDegrees(float[] values){
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.save();
        canvas.rotate(values[0] - initDegree, surfaceWidth / 2, surfaceHeight / 4);
        canvas.drawBitmap(bmpPlate, surfaceWidth / 2 - bmpPlate.getWidth() / 2, surfaceHeight / 4 - bmpPlate.getHeight() / 2, paint);
        canvas.restore();
        StringBuilder sb = new StringBuilder();
        sb.append("( ").append(values[0]).append(" - ").append(initDegree).append(" )");
        canvas.drawText(sb.toString(), 30, surfaceHeight / 4 - 30, paint);
        canvas.drawText(String.valueOf(values[0] - initDegree), 30, surfaceHeight / 4, paint);
        canvas.save();
        canvas.rotate(values[1], surfaceWidth / 2, surfaceHeight / 2);
        canvas.drawBitmap(bmpHeight, surfaceWidth / 2 - bmpHeight.getWidth() / 2, surfaceHeight / 2 - bmpHeight.getHeight() / 2, paint);
        canvas.restore();
        canvas.drawText(String.valueOf(values[1]), 30, surfaceHeight / 2, paint);
        canvas.save();
        canvas.rotate(-values[2], surfaceWidth / 2, surfaceHeight / 4 * 3);
        canvas.drawBitmap(bmpWidth, surfaceWidth / 2 - bmpWidth.getWidth() / 2, surfaceHeight / 4 * 3 - bmpWidth.getHeight() / 2, paint);
        canvas.restore();
        canvas.drawText(String.valueOf(values[2]), 30, surfaceHeight / 4 * 3, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surface_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
