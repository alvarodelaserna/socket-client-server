package com.alvarodelaserna.socket.serverclient.support.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import static android.telephony.TelephonyManager.*;

public class Connectivity {
	
	/**
	 * Get the network info
	 *
	 * @return {@link NetworkInfo NetworkInfo} object containing information about the current
	 * active network
	 *
	 * @see ConnectivityManager#getActiveNetworkInfo()
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
			Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
	/**
	 * Get connection speed
	 *
	 * @return {@link String String} representing the approximate coonection speed of the device
	 *
	 * @see TelephonyManager#getNetworkType()
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
	 * Get GSM network info
	 *
	 * @return {@link JSONObject JSON} object containing the data
	 *
	 * @see TelephonyManager#getNetworkType()
	 * @see TelephonyManager#getNetworkOperator()
	 * @see TelephonyManager#getNetworkOperatorName()
	 * @see TelephonyManager#getSimOperator()
	 * @see TelephonyManager#getSimCountryIso()
	 * @see TelephonyManager#getNetworkCountryIso()
	 * @see TelephonyManager#getSimState()
	 */
	public static JSONObject getGsmInfoObj(Context context) {
		JSONObject result = new JSONObject();
		TelephonyManager manager = (TelephonyManager) context.getSystemService(
			Context.TELEPHONY_SERVICE);
		try {
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
					break;
			}
			result.put("type", netType);
			String networkOperator = manager.getNetworkOperator();
			result.put("networkOperator", StringUtils.isNullOrEmpty(networkOperator) ? "Unavailable"
																					 : networkOperator);
			String networkOperatorName = manager.getNetworkOperatorName();
			result.put("carrierName", StringUtils.isNullOrEmpty(networkOperatorName) ? "Unavailable"
																					 : networkOperatorName);
			String simOperator = manager.getSimOperator();
			result.put("simOperator",
					   StringUtils.isNullOrEmpty(simOperator) ? "Unavailable" : simOperator);
			String simCountryIso = manager.getSimCountryIso();
			result.put("simCountryIso",
					   StringUtils.isNullOrEmpty(simCountryIso) ? "Unavailable" : simCountryIso);
			String networkCountryIso = manager.getNetworkCountryIso();
			result.put("networkCountryIso",
					   StringUtils.isNullOrEmpty(networkCountryIso) ? "Unavailable"
																	: networkCountryIso);
			int simState = manager.getSimState();
			String sim;
			switch (simState) {
				case SIM_STATE_UNKNOWN:
					sim = "Unknown";
					break;
				case SIM_STATE_ABSENT:
					sim = "Absent";
					break;
				case SIM_STATE_PIN_REQUIRED:
					sim = "Locked. PIN is required";
					break;
				case SIM_STATE_PUK_REQUIRED:
					sim = "Locked. PUK is required";
					break;
				case SIM_STATE_NETWORK_LOCKED:
					sim = "Locked. Requires a network PIN to unlock";
					break;
				case SIM_STATE_READY:
					sim = "Present";
					break;
				default:
					// SIM_STATE_NOT_READY
					// SIM_STATE_PERM_DISABLED
					// SIM_STATE_CARD_IO_ERROR
					sim = "Error";
					break;
			}
			result.put("simState", StringUtils.isNullOrEmpty(sim) ? "Unavailable" : sim);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Get current cell information
	 *
	 * @return {@link JSONObject JSON} object containing the data
	 *
	 * @see TelephonyManager#getCellLocation()
	 * @see TelephonyManager#getAllCellInfo()
	 * @see CellInfoGsm#getCellIdentity()
	 * @see CellInfoLte#getCellIdentity()
	 * @see CellInfoCdma#getCellIdentity()
	 * @see CellInfoWcdma#getCellIdentity()
	 * @see CellInfoGsm#getCellSignalStrength()
	 * @see CellInfoLte#getCellSignalStrength()
	 * @see CellInfoCdma#getCellSignalStrength()
	 * @see CellInfoWcdma#getCellSignalStrength()
	 * @see CellIdentityGsm#getCid()
	 * @see CellIdentityGsm#getArfcn()
	 * @see CellIdentityGsm#getBsic()
	 * @see CellIdentityGsm#getLac()
	 * @see CellIdentityGsm#getMcc()
	 * @see CellIdentityGsm#getMnc()
	 * @see CellIdentityLte#getCi()
	 * @see CellIdentityLte#getMnc()
	 * @see CellIdentityLte#getMcc()
	 * @see CellIdentityLte#getEarfcn()
	 * @see CellIdentityLte#getPci()
	 * @see CellIdentityLte#getTac()
	 * @see CellIdentityCdma#getSystemId()
	 * @see CellIdentityCdma#getLatitude()
	 * @see CellIdentityCdma#getBasestationId()
	 * @see CellIdentityCdma#getLongitude()
	 * @see CellIdentityCdma#getNetworkId()
	 * @see CellIdentityWcdma#getCid()
	 * @see CellIdentityWcdma#getMcc()
	 * @see CellIdentityWcdma#getLac()
	 * @see CellIdentityWcdma#getMnc()
	 * @see CellIdentityWcdma#getPsc()
	 * @see CellIdentityWcdma#getUarfcn()
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
							String signalStrength = String.valueOf(cellSignalStrengthGsm.getDbm()) + "dB";
							cellInfoObj.put("strength", signalStrength);
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
							String signalStrength = String.valueOf(cellSignalStrengthLte.getDbm()) + "dB";
							cellInfoObj.put("strength", signalStrength);
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
							String signalStrength = String.valueOf(cellSignalStrengthCdma.getDbm()) + "dB";
							cellInfoObj.put("strength", signalStrength);
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
							String signalStrength = String.valueOf(cellSignalStrengthWcdma.getDbm()) + "dB";
							cellInfoObj.put("strength", signalStrength);
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
			if (cellLocation != null) {
				result.put("location", cellLocation.toString());
			} else {
				result.put("location", "Unavailable");
			}
			if (cellInfoObj.has("type")) {
				result.put("info", cellInfoObj);
			} else {
				result.put("info", "Unavailable");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Enables or disables data network.
	 * Works on devices with API level <= 18
	 *
	 * @see Class#getDeclaredMethod(String, Class[])
	 * @see Method#setAccessible(boolean)
	 * @see Method#invoke(Object, Object...)
	 *
	 * @throws ClassNotFoundException if any of the underlying methods throw this exception
	 * @throws NoSuchFieldException if any of the underlying methods throw this exception
	 * @throws IllegalAccessException if any of the underlying methods throw this exception
	 * @throws NoSuchMethodException if any of the underlying methods throw this exception
	 * @throws InvocationTargetException if any of the underlying methods throw this exception
	 */
	public static void setMobileDataEnabled(Context context, boolean enabled)
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
