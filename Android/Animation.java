// INTENT
	btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
				// R.anim.anim_fadein: file hiệu ứng chạy vào
				// R.anim.anim_fadeout: file hiệu ứng chạy ra
            }
        });
		
// ANIMATION
	1. Tải hiệu ứng từ Resource vào Code animation
    final Animation animationAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_alpha);
	
	2. Chạy hiệu ứng cho VIEW
	imgAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cách 1:
                //imgAvar.startAnimation(animationAlpha);
                // Cách 2:
                view.startAnimation(animationAlpha); // Sử dụng view cũng là chính nó
            }
        });
		
	3. Alpha : 
	android:fillAfter="true"
	<alpha android:fromAlpha="1"
        android:toAlpha="0.2"
        android:duration = "1000"
        android:repeatCount = "2"
        android:repeatMode = "reverse"
        android:startOffset = "1000" : Sau khoảng thời gian này thì mới bắt đầu chạy hiệu ứng
        >
    </alpha>
	
	4. Scale
	
		android:fromXScale="1" : Cần để X = Y thì sẽ phóng cân đối (Phóng từ 1 tới 3)
        android:toXScale="3"
        android:fromYScale="1"
        android:toYScale="3"
		android:pivotX="50%" : Trọng tâm phóng (tính theo % : 50 : 50 là thu/phóng giữa hình)
        android:pivotY="50%"
		
	5. Rotate
		android:toDegrees="0"
        android:fromDegrees="-45"
		android:pivotX="50%" : Trọng tâm quay (tính theo % : 50 : 50 là quay giữa hình)
        android:pivotY="50%"