package com.ariondan.vendor.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.grid.ProductAdapter;
import com.ariondan.vendor.model.ProductModel;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    NfcAdapter nfcAdapter;
    private RecyclerView gridProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_history).setOnClickListener(this);
        gridProducts = (RecyclerView) findViewById(R.id.grid_products);
        List<ProductModel> productModels = new ArrayList<>();
        productModels.add(new ProductModel(1, "Coca", "", 2, "", "soda"));
        productModels.add(new ProductModel(2, "Cola", "", 3.5, "", "soda"));
        productModels.add(new ProductModel(3, "Coke", "", 100, "", "soda"));
        productModels.add(new ProductModel(4, "Big Cola", "", 7.5, "", "soda"));
        productModels.add(new ProductModel(5, "Coca-Cola", "", 5, "", "soda"));
        productModels.add(new ProductModel(6, "Mici", "", 5, "", "food"));
        gridProducts.setLayoutManager(new GridLayoutManager(this, 3));
        gridProducts.setAdapter(new ProductAdapter(this, productModels));

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (ToggleButton) findViewById(R.id.tglReadWrite);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {

        } else
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
    }

    //Method not working
//    private int getColumnsForDisplay() {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        return displayMetrics.widthPixels / layoutItem.getMeasuredWidth();
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NfcIntent", Toast.LENGTH_SHORT).show();

            if (tglReadWrite.isChecked()) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (parcelables != null && parcelables.length > 0) {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                } else Toast.makeText(this, "No NDEF messages fond", Toast.LENGTH_SHORT).show();
            } else {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(txtTagContent.getText() + "");
                writeNdefMessage(tag, ndefMessage);
            }
            super.onNewIntent(intent);
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            txtTagContent.setText(tagContent);


        } else Toast.makeText(this, "No NDEF records fond", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.button_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable", Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                ;
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Tag writen!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());

        }

    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e) {
            Log.e("creatTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage((new NdefRecord[]{ndefRecord}));
        return ndefMessage;
    }

    public void tglReadWriteOnClick(View view) {
        txtTagContent.setText("");
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }
}

