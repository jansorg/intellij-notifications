package dev.ja.samples;

import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

class NotificationConfigBuilder {
    private static final NotificationGroup BALLOON_GROUP = NotificationGroup.balloonGroup("demo.notifications.balloon");
    private static final NotificationGroup STICKY_GROUP = new NotificationGroup("demo.stickyBalloon", NotificationDisplayType.STICKY_BALLOON, true);

    private final Project project;
    @NotNull
    private String title = "";
    private String subtitle;
    @NotNull
    private String content = "";
    @Nullable
    private String dropdownText;
    @Nullable
    private Notification.CollapseActionsDirection collapseDirection;
    @Nullable
    private Icon icon;
    @NotNull
    private NotificationGroup group = BALLOON_GROUP;
    private boolean isFullContent;
    private boolean isImportant;
    @NotNull
    private NotificationType notificationType = NotificationType.INFORMATION;
    private List<AnAction> actions = Lists.newArrayList();

    private NotificationConfigBuilder(Project project) {
        this.project = project;
    }

    static NotificationConfigBuilder create(Project project) {
        return new NotificationConfigBuilder(project);
    }

    static NotificationConfigBuilder create(Project project, NotificationConfig config) {
        NotificationConfigBuilder b = new NotificationConfigBuilder(project);
        b.title = config.title;
        b.subtitle = config.subtitle;
        b.content = config.content;
        b.dropdownText = config.dropdownText;
        b.collapseDirection = config.collapseDirection;
        b.icon = config.icon;
        b.group = config.group;
        b.isFullContent = config.isFullContent;
        b.isImportant = config.isImportant;
        b.notificationType = config.notificationType;

        b.actions.addAll(config.actions);

        return b;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }

    public void setDropdownText(@Nullable String dropdownText) {
        this.dropdownText = dropdownText;
    }

    public void setCollapseDirection(@Nullable Notification.CollapseActionsDirection collapseDirection) {
        this.collapseDirection = collapseDirection;
    }

    public void setIcon(@Nullable Icon icon) {
        this.icon = icon;
    }

    public void setGroup(@NotNull NotificationGroup group) {
        this.group = group;
    }

    public void setFullContent(boolean fullContent) {
        isFullContent = fullContent;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setNotificationType(@NotNull NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void addAction(AnAction actions) {
        this.actions.add(actions);
    }

    Notification build() {
        NotificationConfig config = new NotificationConfig(title,
                subtitle,
                content,
                dropdownText,
                collapseDirection,
                icon,
                group,
                isFullContent,
                isImportant,
                notificationType,
                actions);

        if (this.isFullContent) {
            return new FullContentConfigurableNotification(config);
        }
        return new ConfigurableNotification(config);
    }

    public void resetActions() {
        this.actions.clear();
    }

    public void addDefaultActions() {
        if (this.group.getDisplayType() == NotificationDisplayType.TOOL_WINDOW) {
            // toolwindow notifications don't support actions
            return;
        }

        addAction(ConfigurableNotificationAction.create("Toggle full content", builder -> {
            builder.setFullContent(!builder.isFullContent);
        }));

        addAction(ConfigurableNotificationAction.create("Toggle important", builder -> {
            builder.setImportant(!builder.isImportant);
        }));

        addAction(ConfigurableNotificationAction.create("As information", builder -> {
            builder.setNotificationType(NotificationType.INFORMATION);
        }));
        addAction(ConfigurableNotificationAction.create("As warnings", builder -> {
            builder.setNotificationType(NotificationType.WARNING);
        }));
        addAction(ConfigurableNotificationAction.create("As error", builder -> {
            builder.setNotificationType(NotificationType.ERROR);
        }));

        if (collapseDirection == Notification.CollapseActionsDirection.KEEP_LEFTMOST) {
            addAction(ConfigurableNotificationAction.create("collapse left", builder -> {
                builder.setCollapseDirection(Notification.CollapseActionsDirection.KEEP_RIGHTMOST);
            }));
        } else {
            addAction(ConfigurableNotificationAction.create("collapse right", builder -> {
                builder.setCollapseDirection(Notification.CollapseActionsDirection.KEEP_LEFTMOST);
            }));
        }

        addAction(ConfigurableNotificationAction.create("set title", builder -> {
            String value = Messages.showInputDialog(project, "Title:", "Notification Title", null, builder.title, null);
            if (value != null) {
                builder.setTitle(value);
            }
        }));

        addAction(ConfigurableNotificationAction.create("set subtitle", builder -> {
            String value = Messages.showInputDialog(project, "Subtitle:", "Notification Subtitle", null, builder.subtitle, null);
            if (value != null) {
                builder.setSubtitle(value);
            }
        }));

        addAction(ConfigurableNotificationAction.create("set content", builder -> {
            String value = Messages.showMultilineInputDialog(project, "Content:", "Notification Content", builder.content, builder.icon, null);
            if (value != null) {
                builder.setContent(value);
            }
        }));

        addAction(ConfigurableNotificationAction.create("set dropdown text", builder -> {
            String value = Messages.showInputDialog(project, "Dropdown text:", "Notification Dropdown Text", null, builder.dropdownText, null);
            if (value != null) {
                builder.setDropdownText(value);
            }
        }));

        if (group.getDisplayType() != NotificationDisplayType.STICKY_BALLOON) {
            addAction(ConfigurableNotificationAction.create("sticky", builder -> {
                builder.setGroup(STICKY_GROUP);
            }));
        }
        if (group.getDisplayType() != NotificationDisplayType.BALLOON) {
            addAction(ConfigurableNotificationAction.create("sticky", builder -> {
                builder.setGroup(BALLOON_GROUP);
            }));
        }
        if (group.getDisplayType() != NotificationDisplayType.TOOL_WINDOW) {
            addAction(ConfigurableNotificationAction.create("tool window", builder -> {
                String activeId = ToolWindowManager.getActiveId();
                if (activeId == null) {
                    activeId = ToolWindowManagerEx.getInstance(project).getToolWindowIds()[0];
                }
                builder.setGroup(NotificationGroup.toolWindowGroup("demo.notifications.toolwindow", activeId));
            }));
        }

        if (icon == null) {
            addAction(ConfigurableNotificationAction.create("with icon", builder -> {
                builder.setIcon(PlatformIcons.PROJECT_ICON);
            }));
        } else {
            addAction(ConfigurableNotificationAction.create("no icon", builder -> {
                builder.setIcon(null);
            }));
        }
    }
}
