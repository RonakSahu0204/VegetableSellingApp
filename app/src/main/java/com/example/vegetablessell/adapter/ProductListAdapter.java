package com.example.vegetablessell.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vegetablessell.R;
import com.example.vegetablessell.activity.MainActivity;
import com.example.vegetablessell.fragment.UserHomeFragment;
import com.example.vegetablessell.model.ProductDetails;
import com.example.vegetablessell.utils.SessionManagement;
import com.example.vegetablessell.utils.VegetablesConstant;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductDetails> productList;
    public Context context;
    private LayoutInflater mInflater;
    RequestQueue requestQueue;

    OnBottomReachedListener onBottomReachedListener;

    public ProductListAdapter(List<ProductDetails> productList, Context context) {
        this.context = context;
        this.productList = productList;
        this.mInflater = LayoutInflater.from(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public ProductDetails getItem(int position) {
        return productList.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productListView = mInflater.inflate(R.layout.product_list_view, parent, false);
        return new ProductViewHolder(productListView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ProductDetails product = productList.get(position);
        ProductViewHolder viewHolder = (ProductViewHolder) holder;
        Picasso.with(context)
                .load(R.drawable.a2)
                .placeholder(R.drawable.a2)
                .resize(600, 600)
                .into(viewHolder.productIcon);
        viewHolder.productName.setText(product.getProductName());
        viewHolder.productPrice.setText(product.getPrice()+"");
        viewHolder.productQuantity.setText(product.getQuantity()+"");

        if(position == productList.size() - 1) {
            onBottomReachedListener.onBottomReached(position);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    private class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView productIcon;
        private TextView productName;
        private TextView productPrice;
        private TextView productQuantity, itemCount;
        private ImageButton addToCart, incProdCount, decProdCount;



        ProductListAdapter productListAdapter;

        private ProductViewHolder(View itemView, ProductListAdapter adapter) {
            super(itemView);

            productListAdapter = adapter;
            int itemQuantity = 0;

            productIcon = itemView.findViewById(R.id.itemIcon);
            productName = itemView.findViewById(R.id.itemName);
            productQuantity = itemView.findViewById(R.id.itemQuantity);
            productPrice = itemView.findViewById(R.id.itemPrice);
            addToCart = itemView.findViewById(R.id.addToCart1);
            incProdCount = itemView.findViewById(R.id.plusButton);
            decProdCount = itemView.findViewById(R.id.minusButton);
            itemCount = itemView.findViewById(R.id.enterQuantity1);


            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("add To Cart ", productListAdapter.getItem(getAdapterPosition()).toString());
                    Toast.makeText(itemView.getContext(), productListAdapter.getItem(getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
                   /// UserHomeFragment userHomeFragment = new UserHomeFragment();

                    String productId = productList.get(getAdapterPosition()).getId();

                    int quantity = Integer.parseInt(itemCount.getText().toString());

                    productListAdapter.addToCart(productId, quantity);
                    itemCount.setText("0");
                }
            });

            incProdCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int result = Integer.parseInt(itemCount.getText().toString());
                    itemCount.setText(result+1+"");

                }
            });

            decProdCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int result = Integer.parseInt(itemCount.getText().toString());
                    if(result > 0) {
                        itemCount.setText(result-1+"");
                    }


                }
            });



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("position ", position+"");
            Toast.makeText(v.getContext(), position+"", Toast.LENGTH_SHORT).show();
        }
    }


    public interface OnBottomReachedListener {

        void onBottomReached(int position);

    }


    public void addToCart(String productId, int quantity) {

        String URL = VegetablesConstant.BASE_URL+"/auth/user/addToCart";

        JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.POST, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("response ", response.toString());
                            String status = response.getString("status");

                        } catch (JSONException e) {
                            Log.d("json error", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("json err", error.toString());
                    }
                }) {

            @Override
            public byte[] getBody() {

                HashMap<String, String> params = new HashMap<>();
                params.put("productId", productId);
                params.put("quantity", quantity+"");

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+ SessionManagement.getJwtToken());

                return headers;

            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        arrReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(arrReq);

    }

}
