package tw.nekomimi.nkmr;

import static tw.nekomimi.nekogram.NekoConfig.TITLE_TYPE_TEXT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressLint("ApplySharedPref")
public class NekomuraConfig {
    private static final int configTypeBool = 0;
    private static final int configTypeInt = 1;
    private static final int configTypeString = 2;
    private static final int configTypeSetInt = 3;
    private static final int configTypeMapIntInt = 4;
    private static final int configTypeLong = 5;
    private static final int configTypeFloat = 6;

    public static class ConfigItem {
        private String key;
        private int type;

        private Object defaultValue;
        private Object value;
        private boolean forceDefault;//针对某些人的垃圾选项，应该不允许改变默认值

        private ConfigItem(String k, int t, Object d) {
            key = k;
            type = t;
            defaultValue = d;
        }

        public String getKey() {
            return key;
        }

        // 读配置

        public boolean Bool() {
            return (boolean) value;
        }

        public int Int() {
            return (int) value;
        }

        public Long Long() {
            return (Long) value;
        }

        public Float Float() {
            return (Float) value;
        }

        public String String() {
            return value.toString();
        }

        public HashSet<Integer> SetInt() {
            return (HashSet<Integer>) value;
        }

        public HashMap<Integer, Integer> MapIntInt() {
            return (HashMap<Integer, Integer>) value;
        }

        public boolean SetIntContains(Integer v) {
            return ((HashSet<Integer>) value).contains(v);
        }


        public void changed(Object o) {
            value = o;
        }

        //写配置
        //这里没有检查类型哦

        public boolean toggleConfigBool() {
            value = !this.Bool();
            if (forceDefault) {
                value = defaultValue;
            }
            saveConfig(this);
            return this.Bool();//返回toggle之后的
        }

        public void setConfigBool(boolean v) {
            value = v;
            if (forceDefault) {
                value = defaultValue;
            }
            saveConfig(this);
        }

        public void setConfigInt(int v) {
            if (forceDefault) {
                value = defaultValue;
            }
            value = v;
            saveConfig(this);
        }

        public void setConfigLong(Long v) {
            if (forceDefault) {
                value = defaultValue;
            }
            value = v;
            saveConfig(this);
        }

        public void setConfigFloat(Float v) {
            if (forceDefault) {
                value = defaultValue;
            }
            value = v;
            saveConfig(this);
        }

        public void setConfigString(String v) {
            if (v == null) {
                value = "";
            } else {
                value = v;
            }
            if (forceDefault) {
                value = defaultValue;
            }
            saveConfig(this);
        }

        public void setConfigSetInt(HashSet<Integer> v) {
            value = v;
            if (forceDefault) {
                value = defaultValue;
            }
            saveConfig(this);
        }

        public void setConfigMapInt(HashMap<Integer, Integer> v) {
            value = v;
            saveConfig(this);
        }
    }

    private static boolean configLoaded;
    private static final Object sync = new Object();
    private static final ArrayList<ConfigItem> configs = new ArrayList<>();

    // Configs
    public static ConfigItem migrate = addConfig("NekoConfigMigrate", configTypeBool, false);
    public static ConfigItem largeAvatarInDrawer = addConfig("AvatarAsBackground", configTypeInt, 0); // 0:TG Default 1:NekoX Default 2:Large Avatar
    public static ConfigItem unreadBadgeOnBackButton = addConfig("unreadBadgeOnBackButton", configTypeBool, false);
    public static ConfigItem customPublicProxyIP = addConfig("customPublicProxyIP", configTypeString, "");

    // From NekoConfig
    // TODO sort
    public static ConfigItem useIPv6 = addConfig("IPv6", configTypeBool, false);
    public static ConfigItem hidePhone = addConfig("HidePhone", configTypeBool, true);
    public static ConfigItem ignoreBlocked = addConfig("IgnoreBlocked", configTypeBool, false);
    public static ConfigItem tabletMode = addConfig("TabletMode", configTypeInt, 0);
    public static ConfigItem inappCamera = addConfig("DebugMenuEnableCamera", configTypeBool, false); // fake
    public static ConfigItem smoothKeyboard = addConfig("DebugMenuEnableSmoothKeyboard", configTypeBool, false);// fake

    public static ConfigItem typeface = addConfig("TypefaceUseDefault", configTypeBool, false);
    public static ConfigItem nameOrder = addConfig("NameOrder", configTypeInt, 1);
    public static ConfigItem mapPreviewProvider = addConfig("MapPreviewProvider", configTypeInt, 0);
    public static ConfigItem transparentStatusBar = addConfig("TransparentStatusBar", configTypeBool, false);
    public static ConfigItem residentNotification = addConfig("EnableResidentNotification", configTypeBool, false);
    public static ConfigItem hideProxySponsorChannel = addConfig("HideProxySponsorChannel", configTypeBool, false);
    public static ConfigItem showAddToSavedMessages = addConfig("showAddToSavedMessages", configTypeBool, true);
    public static ConfigItem showReport = addConfig("showReport", configTypeBool, true);
    public static ConfigItem showViewHistory = addConfig("showViewHistory", configTypeBool, true);
    public static ConfigItem showAdminActions = addConfig("showAdminActions", configTypeBool, true);
    public static ConfigItem showChangePermissions = addConfig("showChangePermissions", configTypeBool, true);
    public static ConfigItem showDeleteDownloadedFile = addConfig("showDeleteDownloadedFile", configTypeBool, true);
    public static ConfigItem showMessageDetails = addConfig("showMessageDetails", configTypeBool, false);
    public static ConfigItem showTranslate = addConfig("showTranslate", configTypeBool, true);
    public static ConfigItem showRepeat = addConfig("showRepeat", configTypeBool, false);
    public static ConfigItem showMessageHide = addConfig("showMessageHide", configTypeBool, false);

    public static ConfigItem eventType = addConfig("eventType", configTypeInt, 0);
    public static ConfigItem actionBarDecoration = addConfig("ActionBarDecoration", configTypeInt, 0);
    public static ConfigItem newYear = addConfig("ChristmasHat", configTypeBool, false);
    public static ConfigItem stickerSize = addConfig("stickerSize", configTypeFloat, 14.0f);
    public static ConfigItem unlimitedFavedStickers = addConfig("UnlimitedFavoredStickers", configTypeBool, false);
    public static ConfigItem unlimitedPinnedDialogs = addConfig("UnlimitedPinnedDialogs", configTypeBool, false);
    public static ConfigItem translationProvider = addConfig("translationProvider", configTypeInt, 1);
    public static ConfigItem disablePhotoSideAction = addConfig("DisablePhotoViewerSideAction", configTypeBool, true);
    public static ConfigItem openArchiveOnPull = addConfig("OpenArchiveOnPull", configTypeBool, false);
    public static ConfigItem hideKeyboardOnChatScroll = addConfig("HideKeyboardOnChatScroll", configTypeBool, false);
    public static ConfigItem avatarBackgroundBlur = addConfig("BlurAvatarBackground", configTypeBool, false);
    public static ConfigItem avatarBackgroundDarken = addConfig("DarkenAvatarBackground", configTypeBool, false);
    public static ConfigItem useSystemEmoji = addConfig("EmojiUseDefault", configTypeBool, false);
    public static ConfigItem showTabsOnForward = addConfig("ShowTabsOnForward", configTypeBool, false);
    public static ConfigItem rearVideoMessages = addConfig("RearVideoMessages", configTypeBool, false);
    public static ConfigItem hideAllTab = addConfig("HideAllTab", configTypeBool, false);
    public static ConfigItem pressTitleToOpenAllChats = addConfig("pressTitleToOpenAllChats", configTypeBool, false);

    public static ConfigItem disableChatAction = addConfig("DisableChatAction", configTypeBool, false);
    public static ConfigItem sortByUnread = addConfig("sort_by_unread", configTypeBool, false);
    public static ConfigItem sortByUnmuted = addConfig("sort_by_unmuted", configTypeBool, true);
    public static ConfigItem sortByUser = addConfig("sort_by_user", configTypeBool, true);
    public static ConfigItem sortByContacts = addConfig("sort_by_contacts", configTypeBool, true);

    public static ConfigItem disableUndo = addConfig("DisableUndo", configTypeBool, false);

    public static ConfigItem filterUsers = addConfig("filter_users", configTypeBool, true);
    public static ConfigItem filterContacts = addConfig("filter_contacts", configTypeBool, true);
    public static ConfigItem filterGroups = addConfig("filter_groups", configTypeBool, true);
    public static ConfigItem filterChannels = addConfig("filter_channels", configTypeBool, true);
    public static ConfigItem filterBots = addConfig("filter_bots", configTypeBool, true);
    public static ConfigItem filterAdmins = addConfig("filter_admins", configTypeBool, true);
    public static ConfigItem filterUnmuted = addConfig("filter_unmuted", configTypeBool, true);
    public static ConfigItem filterUnread = addConfig("filter_unread", configTypeBool, true);
    public static ConfigItem filterUnmutedAndUnread = addConfig("filter_unmuted_and_unread", configTypeBool, true);

    public static ConfigItem disableSystemAccount = addConfig("DisableSystemAccount", configTypeBool, false);
    public static ConfigItem disableProxyWhenVpnEnabled = addConfig("DisableProxyWhenVpnEnabled", configTypeBool, false);
    public static ConfigItem skipOpenLinkConfirm = addConfig("SkipOpenLinkConfirm", configTypeBool, false);

    public static ConfigItem ignoreMutedCount = addConfig("IgnoreMutedCount", configTypeBool, true);
    public static ConfigItem useDefaultTheme = addConfig("UseDefaultTheme", configTypeBool, false);
    public static ConfigItem showIdAndDc = addConfig("ShowIdAndDc", configTypeBool, false);

    public static ConfigItem googleCloudTranslateKey = addConfig("GoogleCloudTransKey", configTypeString, "");
    public static ConfigItem cachePath = addConfig("cache_path", configTypeString, "");

    public static ConfigItem translateToLang = addConfig("TransToLang", configTypeString, "zh-CN");
    public static ConfigItem translateInputLang = addConfig("TransInputToLang", configTypeString, "en");

    public static ConfigItem ccToLang = addConfig("opencc_to_lang", configTypeString, "");
    public static ConfigItem ccInputLang = addConfig("opencc_input_to_lang", configTypeString, "");

    public static ConfigItem tabsTitleType = addConfig("TabTitleType", configTypeInt, TITLE_TYPE_TEXT);
    public static ConfigItem confirmAVMessage = addConfig("ConfirmAVMessage", configTypeBool, false);
    public static ConfigItem askBeforeCall = addConfig("AskBeforeCalling", configTypeBool, false);
    public static ConfigItem disableNumberRounding = addConfig("DisableNumberRounding", configTypeBool, false);

    public static ConfigItem useSystemDNS = addConfig("useSystemDNS", configTypeBool, false);
    public static ConfigItem customDoH = addConfig("customDoH", configTypeString, "");
    public static ConfigItem hideProxyByDefault = addConfig("HideProxyByDefault", configTypeBool, false);
    public static ConfigItem useProxyItem = addConfig("UseProxyItem", configTypeBool, true);

    public static ConfigItem disableAppBarShadow = addConfig("DisableAppBarShadow", configTypeBool, false);
    public static ConfigItem mediaPreview = addConfig("MediaPreview", configTypeBool, true);

    public static ConfigItem proxyAutoSwitch = addConfig("ProxyAutoSwitch", configTypeBool, false);

    public static ConfigItem usePersianCalendar = addConfig("UsePersiancalendar", configTypeBool, false);
    public static ConfigItem displayPersianCalendarByLatin = addConfig("DisplayPersianCalendarByLatin", configTypeBool, false);
    public static ConfigItem openPGPApp = addConfig("OpenPGPApp", configTypeString, "");
    public static ConfigItem openPGPKeyId = addConfig("OpenPGPKey", configTypeLong, 0L);

    public static ConfigItem disableVibration = addConfig("DisableVibration", configTypeBool, false);
    public static ConfigItem autoPauseVideo = addConfig("AutoPauseVideo", configTypeBool, false);
    public static ConfigItem disableProximityEvents = addConfig("DisableProximityEvents", configTypeBool, false);

    public static ConfigItem ignoreContentRestrictions = addConfig("ignoreContentRestrictions", configTypeBool, !BuildVars.isPlay);
    public static ConfigItem useChatAttachMediaMenu = addConfig("UseChatAttachEnterMenu", configTypeBool, true);
    public static ConfigItem disableLinkPreviewByDefault = addConfig("DisableLinkPreviewByDefault", configTypeBool, false);
    public static ConfigItem sendCommentAfterForward = addConfig("SendCommentAfterForward", configTypeBool, true);
    public static ConfigItem increaseVoiceMessageQuality = addConfig("IncreaseVoiceMessageQuality", configTypeBool, true);
    public static ConfigItem acceptSecretChat = addConfig("AcceptSecretChat", configTypeBool, true);
    public static ConfigItem disableTrending = addConfig("DisableTrending", configTypeBool, true);
    public static ConfigItem dontSendGreetingSticker = addConfig("DontSendGreetingSticker", configTypeBool, false);
    public static ConfigItem hideTimeForSticker = addConfig("HideTimeForSticker", configTypeBool, false);
    public static ConfigItem takeGIFasVideo = addConfig("TakeGIFasVideo", configTypeBool, false);
    public static ConfigItem maxRecentStickerCount = addConfig("maxRecentStickerCount", configTypeInt, 20);
    public static ConfigItem disableSwipeToNext = addConfig("disableSwipeToNextChannel", configTypeBool, true);
    public static ConfigItem disableRemoteEmojiInteractions = addConfig("disableRemoteEmojiInteractions", configTypeBool, true);
    public static ConfigItem disableChoosingSticker = addConfig("disableChoosingSticker", configTypeBool, false);
    public static ConfigItem hideGroupSticker = addConfig("hideGroupSticker", configTypeBool, false);

    public static ConfigItem disableAutoDownloadingWin32Executable = addConfig("Win32ExecutableFiles", configTypeBool, true);
    public static ConfigItem disableAutoDownloadingArchive = addConfig("ArchiveFiles", configTypeBool, true);

    public static ConfigItem enableStickerPin = addConfig("EnableStickerPin", configTypeBool, false);
    public static ConfigItem useMediaStreamInVoip = addConfig("UseMediaStreamInVoip", configTypeBool, false);
    public static ConfigItem customAudioBitrate = addConfig("customAudioBitrate", configTypeInt, 32);
    public static ConfigItem disableGroupVoipAudioProcessing = addConfig("disableGroupVoipAudioProcessing", configTypeBool, false);


    static {
        loadConfig(false);

        // TODO remove this after some versions.
        if (!migrate.Bool()) {
            migrate.setConfigBool(true);

            // NekoConfig.java read & migrate
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);

            typeface.setConfigBool(preferences.getInt("typeface", 0) != 0);
            nameOrder.setConfigInt(preferences.getInt("nameOrder", 1));
            mapPreviewProvider.setConfigInt(preferences.getInt("mapPreviewProvider", 0));
            transparentStatusBar.setConfigBool(preferences.getBoolean("transparentStatusBar", false));
            residentNotification.setConfigBool(preferences.getBoolean("residentNotification", false));
            hideProxySponsorChannel.setConfigBool(preferences.getBoolean("hideProxySponsorChannel", false));
            showAddToSavedMessages.setConfigBool(preferences.getBoolean("showAddToSavedMessages", true));
            showReport.setConfigBool(preferences.getBoolean("showReport", true));
            showViewHistory.setConfigBool(preferences.getBoolean("showViewHistory", true));
            showAdminActions.setConfigBool(preferences.getBoolean("showAdminActions", true));
            showChangePermissions.setConfigBool(preferences.getBoolean("showChangePermissions", true));
            showDeleteDownloadedFile.setConfigBool(preferences.getBoolean("showDeleteDownloadedFile", true));
            showMessageDetails.setConfigBool(preferences.getBoolean("showMessageDetails", false));
            showTranslate.setConfigBool(preferences.getBoolean("showTranslate", true));
            showRepeat.setConfigBool(preferences.getBoolean("showRepeat", false));
            showMessageHide.setConfigBool(preferences.getBoolean("showMessageHide", false));

            eventType.setConfigInt(preferences.getInt("eventType", 0));
            actionBarDecoration.setConfigInt(preferences.getInt("actionBarDecoration", 0));
            newYear.setConfigBool(preferences.getBoolean("newYear", false));
            stickerSize.setConfigFloat(preferences.getFloat("stickerSize", 14.0f));
            unlimitedFavedStickers.setConfigBool(preferences.getBoolean("unlimitedFavedStickers", false));
            unlimitedPinnedDialogs.setConfigBool(preferences.getBoolean("unlimitedPinnedDialogs", false));
            translationProvider.setConfigInt(preferences.getInt("translationProvider", 1));
            disablePhotoSideAction.setConfigBool(preferences.getBoolean("disablePhotoSideAction", true));
            openArchiveOnPull.setConfigBool(preferences.getBoolean("openArchiveOnPull", false));
            //showHiddenFeature.setConfigBool(preferences.getBoolean("showHiddenFeature", false));
            hideKeyboardOnChatScroll.setConfigBool(preferences.getBoolean("hideKeyboardOnChatScroll", false));
            avatarBackgroundBlur.setConfigBool(preferences.getBoolean("avatarBackgroundBlur", false));
            avatarBackgroundDarken.setConfigBool(preferences.getBoolean("avatarBackgroundDarken", false));
            useSystemEmoji.setConfigBool(preferences.getBoolean("useSystemEmoji", false));
            showTabsOnForward.setConfigBool(preferences.getBoolean("showTabsOnForward", false));
            rearVideoMessages.setConfigBool(preferences.getBoolean("rearVideoMessages", false));
            hideAllTab.setConfigBool(preferences.getBoolean("hideAllTab", false));
            pressTitleToOpenAllChats.setConfigBool(preferences.getBoolean("pressTitleToOpenAllChats", false));

            disableChatAction.setConfigBool(preferences.getBoolean("disable_chat_action", false));
            sortByUnread.setConfigBool(preferences.getBoolean("sort_by_unread", false));
            sortByUnmuted.setConfigBool(preferences.getBoolean("sort_by_unmuted", true));
            sortByUser.setConfigBool(preferences.getBoolean("sort_by_user", true));
            sortByContacts.setConfigBool(preferences.getBoolean("sort_by_contacts", true));

            disableUndo.setConfigBool(preferences.getBoolean("disable_undo", false));

            filterUsers.setConfigBool(preferences.getBoolean("filter_users", true));
            filterContacts.setConfigBool(preferences.getBoolean("filter_contacts", true));
            filterGroups.setConfigBool(preferences.getBoolean("filter_groups", true));
            filterChannels.setConfigBool(preferences.getBoolean("filter_channels", true));
            filterBots.setConfigBool(preferences.getBoolean("filter_bots", true));
            filterAdmins.setConfigBool(preferences.getBoolean("filter_admins", true));
            filterUnmuted.setConfigBool(preferences.getBoolean("filter_unmuted", true));
            filterUnread.setConfigBool(preferences.getBoolean("filter_unread", true));
            filterUnmutedAndUnread.setConfigBool(preferences.getBoolean("filter_unmuted_and_unread", true));

            disableSystemAccount.setConfigBool(preferences.getBoolean("disable_system_account", false));
            disableProxyWhenVpnEnabled.setConfigBool(preferences.getBoolean("disable_proxy_when_vpn_enabled", false));
            skipOpenLinkConfirm.setConfigBool(preferences.getBoolean("skip_open_link_confirm", false));

            ignoreMutedCount.setConfigBool(preferences.getBoolean("ignore_muted_count", true));
            useDefaultTheme.setConfigBool(preferences.getBoolean("use_default_theme", false));
            showIdAndDc.setConfigBool(preferences.getBoolean("show_id_and_dc", false));

            googleCloudTranslateKey.setConfigString(preferences.getString("google_cloud_translate_key", null));
            cachePath.setConfigString(preferences.getString("cache_path", null));

            translateToLang.setConfigString(preferences.getString("trans_to_lang", "zh-CN"));
            translateInputLang.setConfigString(preferences.getString("trans_input_to_lang", "en"));

            ccToLang.setConfigString(preferences.getString("opencc_to_lang", null));
            ccInputLang.setConfigString(preferences.getString("opencc_input_to_lang", null));

            tabsTitleType.setConfigInt(preferences.getInt("tabsTitleType", TITLE_TYPE_TEXT));
            confirmAVMessage.setConfigBool(preferences.getBoolean("confirmAVMessage", false));
            askBeforeCall.setConfigBool(preferences.getBoolean("askBeforeCall", false));
            disableNumberRounding.setConfigBool(preferences.getBoolean("disableNumberRounding", false));

            useSystemDNS.setConfigBool(preferences.getBoolean("useSystemDNS", false));
            customDoH.setConfigString(preferences.getString("customDoH", ""));
            hideProxyByDefault.setConfigBool(preferences.getBoolean("hide_proxy_by_default", false));
            useProxyItem.setConfigBool(preferences.getBoolean("use_proxy_item", true));

            disableAppBarShadow.setConfigBool(preferences.getBoolean("disableAppBarShadow", false));
            mediaPreview.setConfigBool(preferences.getBoolean("mediaPreview", true));

            proxyAutoSwitch.setConfigBool(preferences.getBoolean("proxy_auto_switch", false));

            usePersianCalendar.setConfigBool(preferences.getInt("persian_calendar", 0) != 0);
            displayPersianCalendarByLatin.setConfigBool(preferences.getBoolean("displayPersianCalendarByLatin", false));
            openPGPApp.setConfigString(preferences.getString("openPGPApp", ""));
            openPGPKeyId.setConfigLong(preferences.getLong("openPGPKeyId", 0L));

            disableVibration.setConfigBool(preferences.getBoolean("disableVibration", false));
            autoPauseVideo.setConfigBool(preferences.getBoolean("autoPauseVideo", false));
            disableProximityEvents.setConfigBool(preferences.getBoolean("disableProximityEvents", false));

            ignoreContentRestrictions.setConfigBool(preferences.getBoolean("ignoreContentRestrictions", !BuildVars.isPlay));
            useChatAttachMediaMenu.setConfigBool(preferences.getBoolean("useChatAttachMediaMenu", true));
            disableLinkPreviewByDefault.setConfigBool(preferences.getBoolean("disableLinkPreviewByDefault", false));
            sendCommentAfterForward.setConfigBool(preferences.getBoolean("sendCommentAfterForward", true));
            increaseVoiceMessageQuality.setConfigBool(preferences.getBoolean("increaseVoiceMessageQuality", true));
            acceptSecretChat.setConfigBool(preferences.getBoolean("acceptSecretChat", true));
            disableTrending.setConfigBool(preferences.getBoolean("disableTrending", true));
            dontSendGreetingSticker.setConfigBool(preferences.getBoolean("dontSendGreetingSticker", false));
            hideTimeForSticker.setConfigBool(preferences.getBoolean("hideTimeForSticker", false));
            takeGIFasVideo.setConfigBool(preferences.getBoolean("takeGIFasVideo", false));
            maxRecentStickerCount.setConfigInt(preferences.getInt("maxRecentStickerCount", 20));
            disableSwipeToNext.setConfigBool(preferences.getBoolean("disableSwipeToNext", true));
            disableRemoteEmojiInteractions.setConfigBool(preferences.getBoolean("disableRemoteEmojiInteractions", true));
            disableChoosingSticker.setConfigBool(preferences.getBoolean("disableChoosingSticker", false));

            disableAutoDownloadingWin32Executable.setConfigBool(preferences.getBoolean("disableAutoDownloadingWin32Executable", true));
            disableAutoDownloadingArchive.setConfigBool(preferences.getBoolean("disableAutoDownloadingArchive", true));

            enableStickerPin.setConfigBool(preferences.getBoolean("enableStickerPin", false));
            useMediaStreamInVoip.setConfigBool(preferences.getBoolean("useMediaStreamInVoip", false));
            customAudioBitrate.setConfigInt(preferences.getInt("customAudioBitrate", 32));
            disableGroupVoipAudioProcessing.setConfigBool(preferences.getBoolean("disableGroupVoipAudioProcessing", false));
        }
    }

    public static ConfigItem addConfig(String k, int t, Object d) {
        ConfigItem a = new ConfigItem(k, t, d);
        configs.add(a);
        return a;
    }

    public static ConfigItem findOne(String key) {
        for (int i = 0; i < configs.size(); i++) {
            ConfigItem o = configs.get(i);
            if (key.equals(o.key)) {
                return o;
            }
        }
        return null;
    }

    public static void loadConfig(boolean force) {
        synchronized (sync) {
            if (configLoaded && !force) {
                return;
            }
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nkmrcfg", Activity.MODE_PRIVATE);
            for (int i = 0; i < configs.size(); i++) {
                ConfigItem o = configs.get(i);

                if (o.forceDefault) {
                    o.value = o.defaultValue;
                    continue;
                }

                if (o.type == configTypeBool) {
                    o.value = preferences.getBoolean(o.key, (boolean) o.defaultValue);
                }
                if (o.type == configTypeInt) {
                    o.value = preferences.getInt(o.key, (int) o.defaultValue);
                }
                if (o.type == configTypeLong) {
                    o.value = preferences.getLong(o.key, (Long) o.defaultValue);
                }
                if (o.type == configTypeFloat) {
                    o.value = preferences.getFloat(o.key, (Float) o.defaultValue);
                }
                if (o.type == configTypeString) {
                    o.value = preferences.getString(o.key, (String) o.defaultValue);
                }
                if (o.type == configTypeSetInt) {
                    Set<String> ss = preferences.getStringSet(o.key, new HashSet<>());
                    HashSet<Integer> si = new HashSet<>();
                    for (String s : ss) {
                        si.add(Integer.parseInt(s));
                    }
                    o.value = si;
                }
                if (o.type == configTypeMapIntInt) {
                    String cv = preferences.getString(o.key, "");
                    // Log.e("NC", String.format("Getting pref %s val %s", o.key, cv));
                    if (cv.length() == 0) {
                        o.value = new HashMap<Integer, Integer>();
                    } else {
                        try {
                            byte[] data = Base64.decode(cv, Base64.DEFAULT);
                            ObjectInputStream ois = new ObjectInputStream(
                                    new ByteArrayInputStream(data));
                            o.value = (HashMap<Integer, Integer>) ois.readObject();
                            if (o.value == null) {
                                o.value = new HashMap<Integer, Integer>();
                            }
                            ois.close();
                        } catch (Exception e) {
                            o.value = new HashMap<Integer, Integer>();
                        }
                    }
                }
            }
            configLoaded = true;
        }
    }

    public static void saveConfig(ConfigItem item) {
        synchronized (sync) {
            try {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nkmrcfg", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                for (int i = 0; i < configs.size(); i++) {
                    ConfigItem o = configs.get(i);
                    if (item != null && !item.getKey().equals(o.getKey())) continue;

                    if (o.type == configTypeBool) {
                        editor.putBoolean(o.key, (boolean) o.value);
                    }
                    if (o.type == configTypeInt) {
                        editor.putInt(o.key, (int) o.value);
                    }
                    if (o.type == configTypeLong) {
                        editor.putLong(o.key, (Long) o.value);
                    }
                    if (o.type == configTypeFloat) {
                        editor.putFloat(o.key, (Float) o.value);
                    }
                    if (o.type == configTypeString) {
                        editor.putString(o.key, o.value.toString());
                    }
                    if (o.type == configTypeSetInt) {
                        HashSet<String> ss = new HashSet<>();
                        for (Integer n : (Set<Integer>) o.value) {
                            ss.add(Integer.toString(n));
                        }
                        editor.putStringSet(o.key, ss);
                    }
                    if (o.type == configTypeMapIntInt) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(o.value);
                        oos.close();
                        editor.putString(o.key, Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                    }
                }
                editor.commit();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }
}