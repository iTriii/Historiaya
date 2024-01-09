package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//FOR UPDATE ONLY

public class Store extends AppCompatActivity {

    private static final String PREFS_NAME = "ImageURLPrefs";
    private static final String PRODUCT_PREFS_NAME = "ProductPrefs";
    private static final String PRODUCT_KEY_PREFIX = "product_";
    private static final String PRODUCT_DESCRIPTION_KEY_PREFIX = "product_description_";

    AppCompatRadioButton store_tab, purchases_tab;
    TextView Store, points, Purchases;
    ImageButton back;
    Dialog mdialog;
    Button plus;
    Button B1P1, B2P2, B3P3, B4P4, B5P5, B6P6, B7P7;

    private final ImageView[] phArray = new ImageView[7];
    private final TextView[] Product = new TextView[7];
    private final TextView[] prod = new TextView[5];
    private final TextView[] ProductDescription = new TextView[7];
    private final TextView[] productdescription = new TextView[5];
    private final TextView[] productdes = new TextView[5];
    private final TextView[] pro= new TextView[5];
    private final ImageView[] qrcode = new ImageView[5];
    private final ImageView[] popup_qrcode = new ImageView[5];
    LinearLayout V1, V2, V3, V4, V5;
    LinearLayout v1, v2, v3, v4, v5;
    View storeTabIndicator, purchasesTabIndicator;
    ScrollView StoreScrollView, PurchasesScrollView;
    private int currentPoints = 0;
    private Map<String, Integer> purchaseCounts;
    private static final int PURCHASE_LIMIT = 3;
    private Map<String, Integer> productPrices;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;
    private final StorageReference[] storageRefs = new StorageReference[7];
    private final String[] productIds = {"product1", "product2", "product3", "product4", "product5", "product6", "product7"};
    private static final String VISIBILITY_PREFS_NAME = "VisibilityPrefs";
    private static final String VISIBILITY_KEY_V1 = "visibility_v1";
    private static final String VISIBILITY_KEY_V2 = "visibility_v2";
    private static final String VISIBILITY_KEY_V3 = "visibility_v3";
    private static final String VISIBILITY_KEY_V4 = "visibility_v4";
    private static final String VISIBILITY_KEY_V5 = "visibility_v5";

    private static final int NUM_PRODUCTS = 7;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);
        // Inside your `onCreate` or wherever you initialize Firebase services
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        initializeProductPrices();
        initBuyButtonListeners();
        initializePurchaseCounts();

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> main2());

        store_tab = findViewById(R.id.store_tab);
        purchases_tab = findViewById(R.id.purchases_tab);

        storeTabIndicator = findViewById(R.id.storeTabIndicator);
        purchasesTabIndicator = findViewById(R.id.purchasesTabIndicator);

        StoreScrollView = findViewById(R.id.StoreScrollView);
        PurchasesScrollView = findViewById(R.id.PurchasesScrollView);

        // Set listeners for tab clicks
        store_tab.setOnClickListener(v -> StoreScrollView());
        purchases_tab.setOnClickListener(v -> PurchasesScrollView());

        Store = findViewById(R.id.Store);
        points = findViewById(R.id.points);

        qrcode[0] = findViewById(R.id.qrcode1);
        qrcode[1] = findViewById(R.id.qrcode2);
        qrcode[2] = findViewById(R.id.qrcode3);
        qrcode[3] = findViewById(R.id.qrcode4);
        qrcode[4] = findViewById(R.id.qrcode5);

        popup_qrcode[0] = findViewById(R.id.popup_qrcode1);
        popup_qrcode[1] = findViewById(R.id.popup_qrcode2);
        popup_qrcode[2] = findViewById(R.id.popup_qrcode3);
        popup_qrcode[3] = findViewById(R.id.popup_qrcode4);
        popup_qrcode[4] = findViewById(R.id.popup_qrcode5);


        Product[0] = findViewById(R.id.Product1);
        Product[1] = findViewById(R.id.Product2);
        Product[2] = findViewById(R.id.Product3);
        Product[3] = findViewById(R.id.Product4);
        Product[4] = findViewById(R.id.Product5);
        Product[5] = findViewById(R.id.Product6);
        Product[6] = findViewById(R.id.Product7);

        prod[0] = findViewById(R.id.prod1);
        prod[1] = findViewById(R.id.prod2);
        prod[2] = findViewById(R.id.prod3);
        prod[3] = findViewById(R.id.prod4);
        prod[4] = findViewById(R.id.prod5);

        ProductDescription[0] = findViewById(R.id.ProductDescription1);
        ProductDescription[1] = findViewById(R.id.ProductDescription2);
        ProductDescription[2] = findViewById(R.id.ProductDescription3);
        ProductDescription[3] = findViewById(R.id.ProductDescription4);
        ProductDescription[4] = findViewById(R.id.ProductDescription5);
        ProductDescription[5] = findViewById(R.id.ProductDescription6);
        ProductDescription[6] = findViewById(R.id.ProductDescription7);

        productdescription[0] = findViewById(R.id.productdescription1);
        productdescription[1] = findViewById(R.id.productdescription2);
        productdescription[2] = findViewById(R.id.productdescription3);
        productdescription[3] = findViewById(R.id.productdescription4);
        productdescription[4] = findViewById(R.id.productdescription5);

        productdes[0] = findViewById(R.id.productdes1);
        productdes[1] = findViewById(R.id.productdes2);
        productdes[2] = findViewById(R.id.productdes3);
        productdes[3] = findViewById(R.id.productdes4);
        productdes[4] = findViewById(R.id.productdes5);

        pro[0] = findViewById(R.id.pro1);
        pro[1] = findViewById(R.id.pro2);
        pro[2] = findViewById(R.id.pro3);
        pro[3] = findViewById(R.id.pro4);
        pro[4] = findViewById(R.id.pro5);

        fetchQRCode("productName", "qrcodeImageName", qrcode, popup_qrcode);

        for (int i = 0; i < productIds.length; i++) {
            storageRefs[i] = FirebaseStorage.getInstance().getReference("Products/" + productIds[i] + "/image.jpg");
        }

        for (int i = 0; i < phArray.length; i++) {
            int imageViewId = getResources().getIdentifier("ph" + (i + 1), "id", getPackageName());
            phArray[i] = findViewById(imageViewId);
        }

        if (currentUser != null) {
            retrieveUserPoints();
        }

        V1 = findViewById(R.id.V1);
        V2 = findViewById(R.id.V2);
        V3 = findViewById(R.id.V3);
        V4 = findViewById(R.id.V4);
        V5 = findViewById(R.id.V5);

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v5 = findViewById(R.id.v5);

        mdialog = new Dialog(this);

        sharedPreferences = getSharedPreferences(VISIBILITY_PREFS_NAME, MODE_PRIVATE);

        // Retrieve and set the visibility state for V1
        boolean isV1Visible = getVisibilityState(VISIBILITY_KEY_V1);
        V1.setVisibility(isV1Visible ? View.VISIBLE : View.GONE);

        // Retrieve and set the visibility state for V2
        boolean isV2Visible = getVisibilityState(VISIBILITY_KEY_V2);
        V2.setVisibility(isV2Visible ? View.VISIBLE : View.GONE);

        // Retrieve and set the visibility state for V3
        boolean isV3Visible = getVisibilityState(VISIBILITY_KEY_V3);
        V3.setVisibility(isV3Visible ? View.VISIBLE : View.GONE);

        // Retrieve and set the visibility state for V4
        boolean isV4Visible = getVisibilityState(VISIBILITY_KEY_V4);
        V4.setVisibility(isV4Visible ? View.VISIBLE : View.GONE);

        // Retrieve and set the visibility state for V5
        boolean isV5Visible = getVisibilityState(VISIBILITY_KEY_V5);
        V5.setVisibility(isV5Visible ? View.VISIBLE : View.GONE);

        V1.setOnClickListener(v -> {
            v1.setVisibility(View.VISIBLE);
            saveVisibilityState(VISIBILITY_KEY_V1, true);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
        });
        V2.setOnClickListener(v -> {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.VISIBLE);
            saveVisibilityState(VISIBILITY_KEY_V2, true);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
        });
        V3.setOnClickListener(v -> {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.VISIBLE);
            saveVisibilityState(VISIBILITY_KEY_V3, true);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
        });
        V4.setOnClickListener(v -> {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.VISIBLE);
            saveVisibilityState(VISIBILITY_KEY_V4, true);
            v5.setVisibility(View.GONE);
        });
        V5.setOnClickListener(v -> {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.VISIBLE);
            saveVisibilityState(VISIBILITY_KEY_V5, true);
        });


        plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the points value by 5
                currentPoints += 10;

                // Update the TextView to display the new points value
                points.setText(String.valueOf(currentPoints));

                // Update the points in the database
                updatePointsInDatabase(currentPoints);
            }
        });
        if (currentUser != null) {
            retrieveUserPoints();
            for (int i = 0; i < NUM_PRODUCTS; i++) {
                String productId = "product" + (i + 1);
                fetchProductDataFromFirestore(i, productId, Product[i], ProductDescription[i], prod, productdescription, productdes,pro);
                retrieveProductImage(productId, phArray[i]);
            }
        }
        // Inside your onCreate method or wherever you initialize the qrcode ImageViews
        for (int i = 1; i <= 5; i++) {
            String productName = "Product" + i;
            fetchQRCode(productName, "qrcode" + i, qrcode, popup_qrcode);
        }
    }

    // Define a method to hide a view if touched outside
    private boolean isTouchOutsideView(View view, MotionEvent motionEvent) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();

        return (x < viewX || x > viewX + view.getWidth() || y < viewY || y > viewY + view.getHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchOutsideView(v1, event)) {
                v1.setVisibility(View.GONE);
            }
            if (isTouchOutsideView(v2, event)) {
                v2.setVisibility(View.GONE);
            }
            if (isTouchOutsideView(v3, event)) {
                v3.setVisibility(View.GONE);
            }
            if (isTouchOutsideView(v4, event)) {
                v4.setVisibility(View.GONE);
            }
            if (isTouchOutsideView(v5, event)) {
                v5.setVisibility(View.GONE);
            }
        }
        return super.dispatchTouchEvent(event);
    }


    private boolean getVisibilityState(String viewId) {
        return sharedPreferences.getBoolean(viewId, false);
    }

    private void saveVisibilityState(String viewId, boolean isVisible) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(viewId, isVisible);
        editor.apply();
    }

    private void fetchProductDataFromFirestore(int index, String productId, TextView productNameTextView, TextView productDescriptionTextView, TextView[] prod, TextView[] productdescription,TextView[] pro, TextView[] productdes) {
        String documentName = "Product" + (index + 1); // Adjust the document name as needed

        // Fetch data from Firestore
        db.collection("Products").document(documentName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String productName = documentSnapshot.getString("product_name");
                        String productDescription = documentSnapshot.getString("product_description");

                        // Update UI with fetched data
                        if (productName != null) {
                            productNameTextView.setText(productName);
                            if (prod != null && index < prod.length) {
                                prod[index].setText(productName); // Set in the prod array
                                if (pro != null && index < pro.length) {
                                    pro[index].setText(productName); // Set in the pro array
                                } else {
                                    Log.e("FetchData", "pro array is null or index is out of bounds");
                                }
                            } else {
                                Log.e("FetchData", "prod array is null or index is out of bounds");
                            }
                            // Save product name locally
                            saveProductLocally(productId, productName);
                        } else {
                            Log.e("FetchData", "productName is null");
                        }
                        if (productDescription != null) {
                            // Set the product description in both arrays
                            productDescriptionTextView.setText(productDescription);
                            if (productdescription != null && index < productdescription.length) {
                                productdescription[index].setText(productDescription); // Set in the productdescription array
                            } else {
                                Log.e("FetchData", "productdescription array is null or index is out of bounds");
                            }
                            if (productdes != null && index < productdes.length) {
                                productdes[index].setText(productDescription); // Set in the productdes array
                            } else {
                                Log.e("FetchData", "productdes array is null or index is out of bounds");
                            }
                            // Save product description locally
                            saveProductDescriptionLocally(productId, productDescription);
                        } else {
                            Log.e("FetchData", "productDescription is null");
                        }
                    } else {
                        Log.e("FetchData", "Document does not exist for index: " + index);
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Error fetching product data from Firestore: " + e.getMessage());
                    Log.e("FetchData", "Error fetching product data: " + e.getMessage());
                });
    }

    private void saveProductLocally(String productId, String productName) {
        SharedPreferences productPrefs = getSharedPreferences(PRODUCT_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = productPrefs.edit();
        editor.putString(PRODUCT_KEY_PREFIX + productId, productName);
        editor.apply();
    }

    private void saveProductDescriptionLocally(String productId, String productDescription) {
        SharedPreferences productPrefs = getSharedPreferences(PRODUCT_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = productPrefs.edit();
        editor.putString(PRODUCT_DESCRIPTION_KEY_PREFIX + productId, productDescription);
        editor.apply();
    }


    private void updatePointsInDatabase(int newPoints) {
        String userId = currentUser.getUid();

        // Create a map with the updated HistoriaPoints
        Map<String, Object> pointsMap = new HashMap<>();
        pointsMap.put("HistoriaPoints", newPoints);

        db.collection("users")
                .document(userId)
                .update("HistoriaPoints", newPoints)
                .addOnSuccessListener(aVoid -> {
                    // Database update successful
                    Log.d("StoreActivity", "HistoriaPoints updated in database: " + newPoints);
                    // Add a log or Toast message here
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("StoreActivity", "Error updating HistoriaPoints in database: " + e.getMessage());
                    // Add a log or Toast message here
                });
    }
    private void fetchQRCode(String productName, String qrcodeImageName, ImageView[] qrcode, ImageView[] popup_qrcode) {
        for (int i = 0; i < qrcode.length; i++) {
            final int index = i;
            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReference()
                    .child("QR CODES/Product" + (index + 1) + "/qr_code.png");

            try {
                // Create temporary files for both qrcode and popup_qrcode arrays
                File localFileQr = File.createTempFile("images", "png");
                File localFilePopup = File.createTempFile("images", "png");

                // Retrieve the file from Firebase Storage for qrcode array
                storageRef.getFile(localFileQr)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Load the image into the qrcode ImageView using Glide
                            Glide.with(Store.this)
                                    .load(localFileQr)
                                    .into(qrcode[index]);

                            Log.d("QRCode", "QR code image successfully set to qrcode ImageView " + index);
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors and log the error message
                            Log.e("QRCode", "Error downloading QR code image for qrcode " + index + ": " + e.getMessage());
                        });

                // Retrieve the file from Firebase Storage for popup_qrcode array
                storageRef.getFile(localFilePopup)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Load the image into the popup_qrcode ImageView using Glide
                            Glide.with(Store.this)
                                    .load(localFilePopup)
                                    .into(popup_qrcode[index]);

                            Log.d("QRCode", "QR code image successfully set to popup_qrcode ImageView " + index);
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors and log the error message
                            Log.e("QRCode", "Error downloading QR code image for popup_qrcode " + index + ": " + e.getMessage());
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void retrieveUserPoints() {
        String userId = currentUser.getUid();

        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long userPoints = document.getLong("HistoriaPoints");
                            if (userPoints != null) {
                                currentPoints = userPoints.intValue();
                                points.setText(String.valueOf(currentPoints));

                            }
                        }
                    }
                });
    }

    private void retrieveProductImage(String productId, ImageView imageView) {
        String storagePath = "Products/" + productId + "/image.jpg";

        Log.d("StoreActivity", "Retrieving image for productId: " + productId);
        Log.d("StoreActivity", "Storage path: " + storagePath);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(storagePath);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("StoreActivity", "Download URL for " + productId + ": " + uri);

            // Use Glide to load the image into the specified ImageView
            Glide.with(Store.this)
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(exception -> {
            Log.e("StoreActivity", "Error getting download URL for " + productId, exception);
        });
    }

    private void StoreScrollView() {
        store_tab.setChecked(true);
        purchases_tab.setChecked(false);
        storeTabIndicator.setVisibility(View.VISIBLE);
        purchasesTabIndicator.setVisibility(View.GONE);
        StoreScrollView.setVisibility(View.VISIBLE);
        PurchasesScrollView.setVisibility(View.GONE);
        Store.setText("Store");
    }

    private void PurchasesScrollView() {
        purchases_tab.setChecked(true);
        store_tab.setChecked(false);
        storeTabIndicator.setVisibility(View.GONE);
        purchasesTabIndicator.setVisibility(View.VISIBLE);
        StoreScrollView.setVisibility(View.GONE);
        PurchasesScrollView.setVisibility(View.VISIBLE);
        Store.setText("Purchases");
    }

    public void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    // Inside your `initBuyButtonListeners` method
    private void initBuyButtonListeners() {
        B1P1 = findViewById(R.id.B1P1);
        B2P2 = findViewById(R.id.B2P2);
        B3P3 = findViewById(R.id.B3P3);
        B4P4 = findViewById(R.id.B4P4);
        B5P5 = findViewById(R.id.B5P5);
        B6P6 = findViewById(R.id.B6P6);
        B7P7 = findViewById(R.id.B7P7);

        // Set OnClickListener for purchase actions
        B1P1.setOnClickListener(v -> handlePurchase("store_ph1"));
        B2P2.setOnClickListener(v -> handlePurchase("store_ph2"));
        B3P3.setOnClickListener(v -> handlePurchase("store_ph3"));
        B4P4.setOnClickListener(v -> handlePurchase("store_ph4"));
        B5P5.setOnClickListener(v -> handlePurchase("store_ph5"));
        B6P6.setOnClickListener(v -> handlePurchase("store_ph6"));
        B7P7.setOnClickListener(v -> handlePurchase("store_ph7"));

    }

    private void handlePurchase(String productId) {
        int productPrice = getProductPriceForId(productId);
        Log.d("StoreActivity", "handlePurchase method invoked for product: " + productId);
        Log.d("StoreActivity", "Product Price: " + productPrice);
        Log.d("StoreActivity", "Current Points before purchase: " + currentPoints);

        if (currentPoints >= productPrice) {
            int purchaseCount = purchaseCounts.get(productId);

            if (purchaseCount < PURCHASE_LIMIT) {
                // Deduct points
                currentPoints -= productPrice;
                updatePointsInDatabase(currentPoints);
                points.setText(String.valueOf(currentPoints));
                Log.d("StoreActivity", "Points Deducted. Current Points after purchase: " + currentPoints);

                // Increment the purchase count
                purchaseCount++;
                purchaseCounts.put(productId, purchaseCount);

                // Check if the purchase limit is reached
                if (purchaseCount >= PURCHASE_LIMIT) {
                    // Disable the button and change its appearance
                    disableProductButton(productId);
                }
                    // Set the visibility of the corresponding voucher based on the product ID
                    switch (productId) {
                        case "store_ph1":
                            setVoucherVisibility(V1, true);
                            break;
                        case "store_ph2":
                            setVoucherVisibility(V2, true);
                            break;
                        case "store_ph3":
                            setVoucherVisibility(V3, true);
                            break;
                        case "store_ph4":
                            setVoucherVisibility(V4, true);
                            break;
                        case "store_ph5":
                            setVoucherVisibility(V5, true);
                            break;
                        // Add more cases as needed for additional products
                    }
            } else {
                // User has reached the purchase limit for this product
                Log.d("StoreActivity", "Purchase limit reached for " + productId);
            }
        } else {
            // User doesn't have enough points to buy the product
            Log.d("StoreActivity", "Insufficient points to purchase " + productId);
        }
    }
    private void setVoucherVisibility(View voucherView, boolean isVisible) {
        voucherView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        saveVisibilityState(getVisibilityKeyForVoucher(voucherView), isVisible);
    }
    private String getVisibilityKeyForVoucher(View voucherView) {
        int viewId = voucherView.getId();
        if (viewId == R.id.V1) {
            return VISIBILITY_KEY_V1;
        } else if (viewId == R.id.V2) {
            return VISIBILITY_KEY_V2;
        } else if (viewId == R.id.V3) {
            return VISIBILITY_KEY_V3;
        } else if (viewId == R.id.V4) {
            return VISIBILITY_KEY_V4;
        } else if (viewId == R.id.V5) {
            return VISIBILITY_KEY_V5;
        } else {
            return "";
        }
    }


    private void disableProductButton (String productId){
            Button productButton = findProductButtonById(productId);

            if (productButton != null) {
                productButton.setEnabled(false);
                productButton.setBackgroundResource(R.drawable.price_button_with_outline); // Set the appearance for disabled state
                // You can make further changes to text or other visual

            } else {
                Log.e("StoreActivity", "Invalid product ID: " + productId);
            }
        }

        private int getProductPriceForId (String productId) {
            if (productPrices.containsKey(productId)) {
                return productPrices.get(productId);
            } else {
                Log.e("StoreActivity", "Price not found for " + productId);
                return 0;
            }
        }

        private void initializeProductPrices () {
            productPrices = new HashMap<>();
            productPrices.put("store_ph1", 90);
            productPrices.put("store_ph2", 100);
            productPrices.put("store_ph3", 80);
            productPrices.put("store_ph4", 100);
            productPrices.put("store_ph5", 90);
            productPrices.put("store_ph6", 100);
            productPrices.put("store_ph7", 80);
            // Add more product prices as needed
        }

        private void initializePurchaseCounts () {
            purchaseCounts = new HashMap<>();
            purchaseCounts.put("store_ph1", 0);
            purchaseCounts.put("store_ph2", 0);
            purchaseCounts.put("store_ph3", 0);
            purchaseCounts.put("store_ph4", 0);
            purchaseCounts.put("store_ph5", 0);
            purchaseCounts.put("store_ph6", 0);
            purchaseCounts.put("store_ph7", 0);
            // Add more products and initialize their purchase counts
        }

    private Button findProductButtonById(String productId) {
        switch (productId) {
            case "store_ph1":
                return B1P1;
            case "store_ph2":
                return B2P2;
            case "store_ph3":
                return B3P3;
            case "store_ph4":
                return B4P4;
            case "store_ph5":
                return B5P5;
            case "store_ph6":
                return B6P6;
            case "store_ph7":
                return B7P7;
            // Add more cases as needed for additional products
            default:
                return null; // Invalid product ID
        }
    }
    private void resetVoucherVisibilityPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(VISIBILITY_KEY_V1);
        editor.remove(VISIBILITY_KEY_V2);
        editor.remove(VISIBILITY_KEY_V3);
        editor.remove(VISIBILITY_KEY_V4);
        editor.remove(VISIBILITY_KEY_V5);
        editor.apply();

        // Set the default visibility state for vouchers
        V1.setVisibility(View.VISIBLE);
        V2.setVisibility(View.VISIBLE);
        V3.setVisibility(View.VISIBLE);
        V4.setVisibility(View.VISIBLE);
        V5.setVisibility(View.VISIBLE);
    }

    private void updateUserEmail(String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Email update successful
                            Log.d("StoreActivity", "User email updated to: " + newEmail);

                            // Reset visibility preferences for vouchers
                            resetVoucherVisibilityPreferences();
                        } else {
                            // Email update failed
                            Log.e("StoreActivity", "Error updating user email: " + task.getException());
                        }
                    });
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        finish();
    }
}

