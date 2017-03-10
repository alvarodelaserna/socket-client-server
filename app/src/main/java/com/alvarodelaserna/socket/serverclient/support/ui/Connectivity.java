package com.alvarodelaserna.socket.serverclient.support.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class Connectivity {
	
	/**
	 * Get the network info
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
			Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
	/**
	 * Check if device is connected
	 */
	public static boolean isConnected(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		return (info != null && info.isConnected());
	}
	
	/**
	 * Get connection speed
	 */
	public static String getConnectionSpeed(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(
			Context.TELEPHONY_SERVICE);
		int subType = mTelephonyManager.getNetworkType();
		switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "~ 50-100 kbps";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "~ 14-64 kbps";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "~ 50-100 kbps";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "~ 400-1000 kbps";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "~ 600-1400 kbps";
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "~ 100 kbps";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "~ 2-14 Mbps";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "~ 700-1700 kbps";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "~ 1-23 Mbps";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "~ 400-7000 kbps";
			case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
				return "~ 1-2 Mbps";
			case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
				return "~ 5 Mbps";
			case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
				return "~ 10-20 Mbps";
			case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
				return "~25 kbps";
			case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
				return "~ 10+ Mbps";
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			default:
				return "Unknown";
		}
	}
	
	/**
	 * Get network info
	 */
	public static JSONObject getNetworkInfoObj(Context context) {
		JSONObject result = new JSONObject();
		TelephonyManager manager = (TelephonyManager) context.getSystemService(
			Context.TELEPHONY_SERVICE);
		try {
			result.put("carrierName", manager.getNetworkOperatorName());
			result.put("countryISO", manager.getNetworkCountryIso());
			int networkType = manager.getNetworkType();
			String netType;
			switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					netType = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					netType = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_LTE:
					netType = "4G";
					break;
				default:
					netType = "Unknown";
			}
			result.put("type", netType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Get current cell information
	 */
	public static JSONObject getCellInfo(Context context) {
		JSONObject result = new JSONObject();
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
			Context.TELEPHONY_SERVICE);
		CellLocation cellLocation = telephonyManager.getCellLocation();
		List<CellInfo> all = telephonyManager.getAllCellInfo();
		JSONObject cellInfoObj = new JSONObject();
		CellIdentityGsm cellIdentityGsm = null;
		CellIdentityLte cellIdentityLte = null;
		CellIdentityCdma cellIdentityCdma = null;
		CellIdentityWcdma cellIdentityWcdma = null;
		CellSignalStrengthGsm cellSignalStrengthGsm = null;
		CellSignalStrengthLte cellSignalStrengthLte = null;
		CellSignalStrengthCdma cellSignalStrengthCdma = null;
		CellSignalStrengthWcdma cellSignalStrengthWcdma = null;
		if (!all.isEmpty()) {
			for (int i = 0; i < all.size(); i++) {
				try {
					CellInfoGsm cellinfogsm = (CellInfoGsm) all.get(i);
					cellIdentityGsm = cellinfogsm.getCellIdentity();
					cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
				} catch (ClassCastException e) {
					try {
						CellInfoLte cellInfoLte = (CellInfoLte) all.get(i);
						cellIdentityLte = cellInfoLte.getCellIdentity();
						cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
					} catch (ClassCastException e1) {
						try {
							CellInfoCdma cellInfoCdma = (CellInfoCdma) all.get(i);
							cellIdentityCdma = cellInfoCdma.getCellIdentity();
							cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
						} catch (ClassCastException e2) {
							try {
								CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) all.get(i);
								cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
								cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
							} catch (ClassCastException e3) {
								e.printStackTrace();
							}
						}
					}
				}
				if (cellIdentityGsm != null) {
					try {
						cellInfoObj.put("type", "GSM");
						cellInfoObj.put("cid", cellIdentityGsm.getCid());
						cellInfoObj.put("lac", cellIdentityGsm.getLac());
						cellInfoObj.put("mcc", cellIdentityGsm.getMcc());
						cellInfoObj.put("mnc", cellIdentityGsm.getMnc());
						if (cellSignalStrengthGsm != null) {
							cellInfoObj.put("strength", cellSignalStrengthGsm.getDbm());
						}
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							cellInfoObj.put("arfcn", cellIdentityGsm.getArfcn());
							cellInfoObj.put("bsic", cellIdentityGsm.getBsic());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (cellIdentityLte != null) {
					try {
						cellInfoObj.put("type", "LTE");
						cellInfoObj.put("ci", cellIdentityLte.getCi());
						cellInfoObj.put("mnc", cellIdentityLte.getMnc());
						cellInfoObj.put("mcc", cellIdentityLte.getMcc());
						cellInfoObj.put("pci", cellIdentityLte.getPci());
						cellInfoObj.put("tac", cellIdentityLte.getTac());
						if (cellSignalStrengthLte != null) {
							cellInfoObj.put("strength", cellSignalStrengthLte.getDbm());
						}
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							cellInfoObj.put("earfcn", cellIdentityLte.getEarfcn());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (cellIdentityCdma != null) {
					try {
						cellInfoObj.put("type", "CDMA");
						cellInfoObj.put("systemId", cellIdentityCdma.getSystemId());
						cellInfoObj.put("baseStationId", cellIdentityCdma.getBasestationId());
						cellInfoObj.put("networkId", cellIdentityCdma.getNetworkId());
						cellInfoObj.put("latitude", cellIdentityCdma.getLatitude());
						cellInfoObj.put("longitude", cellIdentityCdma.getLongitude());
						if (cellSignalStrengthCdma != null) {
							cellInfoObj.put("strength", cellSignalStrengthCdma.getDbm());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (cellIdentityWcdma != null) {
					try {
						cellInfoObj.put("type", "WCDMA");
						cellInfoObj.put("cid", cellIdentityWcdma.getCid());
						cellInfoObj.put("mcc", cellIdentityWcdma.getMcc());
						cellInfoObj.put("mnc", cellIdentityWcdma.getMnc());
						cellInfoObj.put("lac", cellIdentityWcdma.getLac());
						cellInfoObj.put("psc", cellIdentityWcdma.getPsc());
						if (cellSignalStrengthWcdma != null) {
							cellInfoObj.put("strength", cellSignalStrengthWcdma.getDbm());
						}
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							cellInfoObj.put("uarfcn", cellIdentityWcdma.getUarfcn());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			result.put("location", cellLocation.toString());
			if (cellInfoObj.has("type")) {
				result.put("info", cellInfoObj);
			} else {
				result.put("info", "Unavailable for this device");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static class GetPublicIPAddressTask extends AsyncTask<Void, Void, String> {
		
		private String server_response;
		
		@Override
		protected String doInBackground(Void... args) {
			URL url;
			HttpURLConnection urlConnection;
			try {
				url = new URL("http://whatismyip.akamai.com/");
				urlConnection = (HttpURLConnection) url.openConnection();
				int responseCode = urlConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					server_response = readStream(urlConnection.getInputStream());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return server_response;
		}
		
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
		}
	}
	// Converting InputStream to String
	
	private static String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response.toString();
	}
	
	public void setMobileDataEnabled(Context context, boolean enabled)
		throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException,
			   NoSuchMethodException, InvocationTargetException {
		final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(
			Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass()
													.getName());
		final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
		connectivityManagerField.setAccessible(true);
		final Object connectivityManager = connectivityManagerField.get(conman);
		final Class connectivityManagerClass = Class.forName(connectivityManager.getClass()
																 .getName());
		final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod(
			"setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);
		setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
	}
}
