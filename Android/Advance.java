//UI
//TỰ ĐỘNG THÊM DẤU ... VÀO SAU NỘI DUNG TEXTVIEW KHI NỘI DUNG VƯỢT QUÁ ĐỘ DÀI
	<TextView
        android:id="@+id/tv_titlebar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:ellipsize="end"
        android:singleLine="true" />
		
		Note: Bên code Java: txtView.setSelected(true);
// TẠO AUTOSCROLL TEXTVIEW KHI NỘI DUNG CỦA NÓ VƯỢT QUÁ CHIỀU NGANG
	<TextView
        android:id="@+id/tv_titlebar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:ellipsize="marquee"
        android:fadingEdge="horizontal"
        android:marqueeRepeatLimit="marquee_forever"
        android:singleLine="true" />
		
		Note: Bên code java: txtView.setSelected(true);
	
// Tách chuỗi từ srt XML
Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

// CHANGE FONTS
1. Thêm thư mục assets/fonts _. Copy font vào
2. Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HLBLONG.TTF"); // Khai báo fonts
   txtName.setTypeface(typeface);// Sử dụng cho View nào
   
 // RADIUS IMAGEVIEW
 
 1. Add Class RoundedTransformation
		import android.graphics.Bitmap;
		import android.graphics.Bitmap.Config;
		import android.graphics.BitmapShader;
		import android.graphics.Canvas;
		import android.graphics.Paint;
		import android.graphics.RectF;
		import android.graphics.Shader;
		  
		// enables hardware accelerated rounded corners
		// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
		public class RoundedTransformation implements com.squareup.picasso.Transformation {
			private final int radius;
			private final int margin;  // dp
		  
			// radius is corner radii in dp
			// margin is the board in dp
			public RoundedTransformation(final int radius, final int margin) {
				this.radius = radius;
				this.margin = margin;
			}
		  
			@Override
			public Bitmap transform(final Bitmap source) {
				final Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
		  
				Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(output);
				canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);
		  
				if (source != output) {
					source.recycle();
				}
		  
				return output;
			}
		  
			@Override
			public String key() {
				return "rounded";
			}
		}
		
2. Sử dụng Picasso
	Picasso.with(getApplicationContext())
                .load(user.getAvartar())
                .transform(new RoundedTransformation(200, 4)) // Redius imageView nhe!
                .into(imgViewIcon);
				
				
// KHÔNG CHO BÀN PHÍM ẢO TRÀN LÊN. Android manifest
	android:windowSoftInputMode="adjustPan"
