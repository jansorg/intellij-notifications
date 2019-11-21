package dev.ja.samples;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Container to store the configuration of a notification.
 */
class NotificationConfig {
    @NotNull
    String title;
    String subtitle;
    @NotNull
    String content;
    @Nullable
    String dropdownText;
    @Nullable
    Notification.CollapseActionsDirection collapseDirection;
    Icon icon;
    @NotNull
    NotificationGroup group;
    boolean isFullContent;
    boolean isImportant;
    @NotNull
    NotificationType notificationType;
    List<AnAction> actions;

    NotificationConfig(@NotNull String title, String subtitle, @NotNull String content, @Nullable String dropdownText,
                       @Nullable Notification.CollapseActionsDirection collapseDirection, Icon icon,
                       @NotNull NotificationGroup group, boolean isFullContent, boolean isImportant,
                       @NotNull NotificationType notificationType, List<AnAction> actions) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.dropdownText = dropdownText;
        this.collapseDirection = collapseDirection;
        this.icon = icon;
        this.group = group;
        this.isFullContent = isFullContent;
        this.isImportant = isImportant;
        this.notificationType = notificationType;
        this.actions = actions;
    }
}
