package dev.ja.samples;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ShowNotificationActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        DemoNotificationService.getInstance().showWithDefaultSettings(project);
    }
}
