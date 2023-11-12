package com.example.log_in;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;



public class Store extends AppCompatActivity {

    AppCompatRadioButton store_tab, purchases_tab;
    TextView Store, points, Purchases;
    ImageButton back;
    Dialog mdialog;
    Button plus;
    Button B1P1, B2P2, B3P3, B4P4, B5P5, B6P6, B7P7;
    private final ImageView[] phArray = new ImageView[7];
    LinearLayout V1, V2, V3, V4, V5;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
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


        for (int i = 0; i < phArray.length; i++) {
            int imageViewId = getResources().getIdentifier("ph" + (i + 1), "id", getPackageName());
            phArray[i] = findViewById(imageViewId);
        }

        if (currentUser != null) {
            retrieveUserPoints();
            retrieveProductImages();
        }
        V1 = findViewById(R.id.V1);
        V2 = findViewById(R.id.V2);
        V3 = findViewById(R.id.V3);
        V4 = findViewById(R.id.V4);
        V5 = findViewById(R.id.V5);
        mdialog = new Dialog(this);
        V1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.activity_v1);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });
        V2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.activity_v2);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });

        V3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.activity_v3);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });

        V4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.activity_v4);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });

        V5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.activity_v5);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });

        Button plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the points value by 5
                currentPoints += 5;

                // Update the TextView to display the new points value
                points.setText(String.valueOf(currentPoints));

                // Update the points in the database
                updatePointsInDatabase(currentPoints);
            }
        });

        if (currentUser != null) {
            retrieveUserPoints();
            retrieveProductImages();
        }
    }

    private void updatePointsInDatabase(int newPoints) {
        String userId = currentUser.getUid();

        // Create a map with the updated HistoriaPoints
        Map<String, Object> pointsMap = new HashMap<>();
        pointsMap.put("HistoriaPoints", newPoints);

        // Update only the "HistoriaPoints" field in the Firestore document
        db.collection("users")
                .document(userId)
                .update("HistoriaPoints", newPoints)
                .addOnSuccessListener(aVoid -> {
                    // Database update successful
                    Log.d("StoreActi    vity", "HistoriaPoints updated in database: " + newPoints);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("StoreActivity", "Error updating HistoriaPoints in database: " + e.getMessage());
                });
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

                                // Retrieve image references from Firestore
                                retrieveProductImages();
                            }
                        }
                    }
                });
    }

    private void retrieveProductImages() {
        for (int i = 0; i < phArray.length; i++) {
            String productId = "ph" + (i + 1);
            String storagePath = "Products/product" + (i + 1) + "/" + productId + ".jpg";

            Log.d("StoreActivity", "Retrieving image for productId: " + productId);
            Log.d("StoreActivity", "Storage path: " + storagePath);

            StorageReference storageRef = FirebaseStorage.getInstance().getReference(storagePath);

            final int index = i;

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.d("StoreActivity", "Download URL for " + productId + ": " + uri);

                // Use Glide to load the image into the ImageView
                Glide.with(Store.this)
                        .load(uri)
                        .into(phArray[index]);
            }).addOnFailureListener(exception -> {
                Log.e("StoreActivity", "Error getting download URL for " + productId, exception);
            });
        }
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
            } else {
                // User has reached the purchase limit for this product
                Log.d("StoreActivity", "Purchase limit reached for " + productId);
            }
        } else {
            // User doesn't have enough points to buy the product
            Log.d("StoreActivity", "Insufficient points to purchase " + productId);
        }
    }

    private void disableProductButton(String productId) {
        Button productButton = findProductButtonById(productId);

        if (productButton != null) {
            productButton.setEnabled(false);
            productButton.setBackgroundResource(R.drawable.price_button_with_outline); // Set the appearance for disabled state
            // You can make further changes to text or other visual properties within the button if needed
        } else {
            Log.e("StoreActivity", "Invalid product ID: " + productId);
        }
    }

    private int getProductPriceForId(String productId) {
        if (productPrices.containsKey(productId)) {
            return productPrices.get(productId);
        } else {
            Log.e("StoreActivity", "Price not found for " + productId);
            return 0;
        }
    }

    private void initializeProductPrices() {
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

    private void initializePurchaseCounts() {
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
}
