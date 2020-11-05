package com.example.vegetablessell.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vegetablessell.R;
import com.example.vegetablessell.activity.MainActivity;
import com.example.vegetablessell.activity.RegisterUser;
import com.example.vegetablessell.adapter.ProductListAdapter;
import com.example.vegetablessell.model.ProductDetails;
import com.example.vegetablessell.utils.SessionManagement;
import com.example.vegetablessell.utils.VegetablesConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;

    private List<ProductDetails> productList = new ArrayList<>();

    private RecyclerView productRecyclerView;

    private ProductListAdapter productListAdapter;

    private static int pageCount = 1;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());

        productRecyclerView = view.findViewById(R.id.recyclerViewProduct);
        productRecyclerView.setClickable(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        productRecyclerView.setLayoutManager(mLayoutManager);
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());




        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

       /* new Handler().post(new Runnable() {
            @Override
            public void run() {*/
                getProductDetails(pageCount);
         /*   }
        });*/

        productListAdapter = new ProductListAdapter(productList, getContext());
        productRecyclerView.setAdapter(productListAdapter);


        productListAdapter.setOnBottomReachedListener(new ProductListAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                getProductDetails(++pageCount);
            }
        });


    }

    private void getProductDetails(int pageCount) {

        String URL = VegetablesConstant.BASE_URL+"/product/user/api/products?page="+pageCount;
        Log.d("URL response ", pageCount+"");

        JsonObjectRequest addToCartReq = new JsonObjectRequest(Request.Method.GET, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("response ", response.toString());
                            String status = response.getString("status");
                            if(!"success".equals(status)) {
                                Toast.makeText(getActivity(), "dc", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            int productListSize = response.getInt("productListSize");

                            JSONArray productArray = response.getJSONArray("productList");
                            for(int i = 0; i < productArray.length(); i++) {
                                JSONObject product = productArray.getJSONObject(i);
                                JSONObject seller = product.getJSONObject("sellerId");
                                productList.add(new ProductDetails(product.getString("_id"),  product.getString("productName"), product.getInt("price"), product.getInt("discount"), product.getInt("quantity"),
                                         product.getString("description"), seller.getString("sellerShopName"), seller.getString("email"), ""));
                                Log.d("list", i+"");
                            }
                            if(productArray.length() > 0) {
                                productListAdapter.notifyItemRangeInserted(productList.size() - productListSize, productListSize);
                            }

                            progressDialog.dismiss();

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
            public String getBodyContentType() {
                return "application/json";
            }
        };

        addToCartReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(addToCartReq);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }


}