package com.agroscan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefsHelper {

    private static final String PREF_USERS = "agroscan_users";
    private static final String PREF_SESSION = "agroscan_session";
    private static final String PREF_SCANS = "agroscan_scans";
    private static final String PREF_HISTORY = "agroscan_history";

    // ─── SESSION ───────────────────────────────────────────────────────────────

    public static void saveSession(Context ctx, String username) {
        ctx.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE)
                .edit().putString("current_user", username).apply();
    }

    public static String getSession(Context ctx) {
        return ctx.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE)
                .getString("current_user", null);
    }

    public static void clearSession(Context ctx) {
        ctx.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE)
                .edit().clear().apply();
    }

    // ─── USERS ─────────────────────────────────────────────────────────────────

    public static void saveUser(Context ctx, String username, String password,
                                String fullName, String email) {
        SharedPreferences.Editor ed =
                ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE).edit();
        ed.putString("user_" + username + "_pass", password);
        ed.putString("user_" + username + "_name", fullName);
        ed.putString("user_" + username + "_email", email);
        ed.putBoolean("user_" + username + "_isAdmin", false);

        Set<String> users = getUsernameSet(ctx);
        users.add(username);
        ed.putStringSet("all_users", users);
        ed.apply();
    }

    public static boolean validateUser(Context ctx, String username, String password) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE);
        String storedPass = prefs.getString("user_" + username + "_pass", null);
        return password.equals(storedPass);
    }

    public static boolean isAdmin(Context ctx, String username) {
        return ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE)
                .getBoolean("user_" + username + "_isAdmin", false);
    }

    public static String getFullName(Context ctx, String username) {
        return ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE)
                .getString("user_" + username + "_name", username);
    }

    public static String getEmail(Context ctx, String username) {
        return ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE)
                .getString("user_" + username + "_email", "");
    }

    public static Set<String> getUsernameSet(Context ctx) {
        Set<String> stored = ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE)
                .getStringSet("all_users", null);
        return stored != null ? new HashSet<>(stored) : new HashSet<>();
    }

    public static void deleteUser(Context ctx, String username) {
        SharedPreferences.Editor ed =
                ctx.getSharedPreferences(PREF_USERS, Context.MODE_PRIVATE).edit();
        ed.remove("user_" + username + "_pass");
        ed.remove("user_" + username + "_name");
        ed.remove("user_" + username + "_email");
        ed.remove("user_" + username + "_isAdmin");
        Set<String> users = getUsernameSet(ctx);
        users.remove(username);
        ed.putStringSet("all_users", users);
        ed.apply();
    }

    public static boolean userExists(Context ctx, String username) {
        return getUsernameSet(ctx).contains(username);
    }

    // ─── SCANS ─────────────────────────────────────────────────────────────────

    public static String saveScan(Context ctx, String plantName, String cropType,
                                   String symptoms, String diagnosis, String date) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_SCANS, Context.MODE_PRIVATE);
        String scanId = "scan_" + System.currentTimeMillis();
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(scanId + "_plant", plantName);
        ed.putString(scanId + "_crop", cropType);
        ed.putString(scanId + "_symptoms", symptoms);
        ed.putString(scanId + "_diagnosis", diagnosis);
        ed.putString(scanId + "_date", date);
        ed.putBoolean(scanId + "_isHealthy", diagnosis.equals("Sana"));

        Set<String> scanIds = getScanIds(ctx);
        scanIds.add(scanId);
        ed.putStringSet("all_scan_ids", scanIds);
        ed.apply();
        return scanId;
    }

    public static Set<String> getScanIds(Context ctx) {
        Set<String> stored = ctx.getSharedPreferences(PREF_SCANS, Context.MODE_PRIVATE)
                .getStringSet("all_scan_ids", null);
        return stored != null ? new HashSet<>(stored) : new HashSet<>();
    }

    public static String getScanField(Context ctx, String scanId, String field) {
        return ctx.getSharedPreferences(PREF_SCANS, Context.MODE_PRIVATE)
                .getString(scanId + "_" + field, "");
    }

    public static boolean isScanHealthy(Context ctx, String scanId) {
        return ctx.getSharedPreferences(PREF_SCANS, Context.MODE_PRIVATE)
                .getBoolean(scanId + "_isHealthy", true);
    }

    // ─── HISTORY ───────────────────────────────────────────────────────────────

    public static void saveHistory(Context ctx, String scanId, String disease,
                                   String treatment, String observations, String date) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_HISTORY, Context.MODE_PRIVATE);
        String histId = "hist_" + System.currentTimeMillis();
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(histId + "_scanId", scanId);
        ed.putString(histId + "_disease", disease);
        ed.putString(histId + "_treatment", treatment);
        ed.putString(histId + "_observations", observations);
        ed.putString(histId + "_date", date);

        Set<String> histIds = getHistoryIdsForScan(ctx, scanId);
        histIds.add(histId);
        ed.putStringSet("hist_for_" + scanId, histIds);
        ed.apply();
    }

    public static Set<String> getHistoryIdsForScan(Context ctx, String scanId) {
        Set<String> stored = ctx.getSharedPreferences(PREF_HISTORY, Context.MODE_PRIVATE)
                .getStringSet("hist_for_" + scanId, null);
        return stored != null ? new HashSet<>(stored) : new HashSet<>();
    }

    public static String getHistoryField(Context ctx, String histId, String field) {
        return ctx.getSharedPreferences(PREF_HISTORY, Context.MODE_PRIVATE)
                .getString(histId + "_" + field, "");
    }

    public static void deleteHistoryEntry(Context ctx, String scanId, String histId) {
        SharedPreferences.Editor ed =
                ctx.getSharedPreferences(PREF_HISTORY, Context.MODE_PRIVATE).edit();
        ed.remove(histId + "_scanId");
        ed.remove(histId + "_disease");
        ed.remove(histId + "_treatment");
        ed.remove(histId + "_observations");
        ed.remove(histId + "_date");
        Set<String> ids = getHistoryIdsForScan(ctx, scanId);
        ids.remove(histId);
        ed.putStringSet("hist_for_" + scanId, ids);
        ed.apply();
    }

    // ─── STATS ─────────────────────────────────────────────────────────────────

    public static int getTotalScans(Context ctx) {
        return getScanIds(ctx).size();
    }

    public static int getHealthyCount(Context ctx) {
        int count = 0;
        for (String id : getScanIds(ctx)) {
            if (isScanHealthy(ctx, id)) count++;
        }
        return count;
    }

    public static int getDiseaseCount(Context ctx) {
        return getTotalScans(ctx) - getHealthyCount(ctx);
    }
}
