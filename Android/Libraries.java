// Thư viện Picasso: Thư viện xử lý load ảnh (cả local và internet)

1. Add Thư viện: 
	compile 'com.squareup.picasso:picasso:2.5.2'

2. Thêm hình từ Internet Cơ bản:
	Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
3. Các hàm khác:
	.load("http://i.imgur.com/DvpvklR.png"): Đường link của hình trên Internet
	.resize(50, 50) : Thay đổi kích thước hình khi tải về (ít dùng)
	.centerCrop() : Kiểu đổ hình lên ImageView là: centerCrop
	.placeholder(R.drawable.user_placeholder) : Hiên hình này khi chưa kịp load hình lên (hình nặng)
	.error(R.drawable.user_placeholder_error) : Khi download hình bị lỗi thì hiện hình này
	.into(imageView); : Nơi gắn Hình vào trên App (Backgroud, ImageView...)

// THƯ VIỆN VOLLEY : TẢI DỮ LIỆU TỪ INTERNER VỀ APP: String, Bitmap, JSON...

1. Add thư viện:
	compile 'com.android.volley:volley:1.0.0'

2. Khai báo chung:
	RequestQueue requestQueue = Volley.newRequestQueue(this); // Sử dụng thư viện Volley // Khai báo hàng đợi
	
	
3. String
	// Lấy nội dung về: dùng Request.Method.GET
	// Tải nội dung lên server: Request.Method.PUST
	String url = "https://www.google.com.vn/"; // Đường dẫn muốn đọc
	stringrequest stringrequest = new stringrequest(Request.Method.GET, url,
                new response.listener<string>() {
                   @override
                    public void onresponse(string response) {
                        toast.maketext(mainactivity.this, response, toast.length_short).show();
                   }
                },
                new response.errorlistener() {
            @override
            public void onerrorresponse(volleyerror error) {
                toast.maketext(mainactivity.this, error.tostring(), toast.length_short).show();
           }
    requestqueue.add(stringrequest); // đưa request vào để xử lý để thực thi.
	
4. Bitmap
	String linkHinh = "http://vanhienblog.com/wp-content/uploads/2016/10/hinh-anh-meo-con.jpg";
        final ImageRequest imageRequest = new ImageRequest(linkHinh,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imgHinh.setImageBitmap(response);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imgHinh.setImageResource(R.mipmap.ic_launcher);
                    }
                });
    requestQueue.add(imageRequest);

5. JSON Array
	String jsonArray = "http://khoapham.vn/KhoaPhamTraining/json/tien/demo4.json";
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, jsonArray, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i< response.length();i++) {
                            try {
                                JSONObject objectKhoaHoc = response.getJSONObject(i);
                                String _khoahoc = objectKhoaHoc.getString("khoahoc");
                                String _hocphi = objectKhoaHoc.getString("hocphi");

                                txtView.append("Tên khóa học: "_khoahoc + "\n" + "Học phí: " + _hocphi);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    requestQueue.add(jsonArrayRequest);
6. JSON Object
		String linkJsonObject = "http://khoapham.vn/KhoaPhamTraining/json/tien/demo1.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, linkJsonObject, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String _monhoc = response.getString("monhoc");                         
                            Toast.makeText(MainActivity.this, _monhoc, Toast.LENGTH_SHORT).show();                          
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

	requestQueue.add(jsonObjectRequest);
		

// Một đống thư viện bên Firebase nữa nhé! Tham khảo firebase.google.com 		
	