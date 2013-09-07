package com.intellij.plugins.haxe.config;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.plugins.haxe.ide.module.HaxeModuleSettings;
import com.intellij.plugins.haxe.ide.module.HaxeModuleType;
import icons.HaxeIcons;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenFLConfig extends AnAction {

  private JBPopup popup;
  private Project project;
  private JPanel panel;
  private Integer panelWidth;

  public OpenFLConfig() {
    super("OpenFLConfig","Item description", HaxeIcons.Haxe_24);
  }

  @Override
  public void actionPerformed(AnActionEvent event) {

    project = event.getData(PlatformDataKeys.PROJECT);
    Component button = event.getInputEvent().getComponent();

    showPopup(project, "Test", button);
  }

  private void showPopup(Project projecty, final String initText, Component comp) {

    panelWidth = 120;

    panel = new JPanel(new HorizontalLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10) );

    ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel)
      .setCancelOnClickOutside(true)
      .setTitle("OpenFL Targets:")
      .setResizable(true)
      .setMovable(true)
      .setRequestFocus(true)
      .setMayBeParent(true);

    popup = builder.createPopup();

    final Module[] modules = ModuleManager.getInstance(project).getModules();
    for (final Module module : modules) {
      if (ModuleType.get(module) == HaxeModuleType.getInstance()) {
        addModuleTargetUI(module);
      }
    }

    popup.showUnderneathOf(comp);
  }

  private void addModuleTargetUI(Module module) {

    JPanel modulePanel = new JPanel(new VerticalLayout());
    modulePanel.setBorder(BorderFactory.createEtchedBorder());
    panel.add(modulePanel);

    JLabel label00 = new JLabel(module.getName());
    label00.setPreferredSize(new Dimension(panelWidth,30));
    label00.setBorder(new EmptyBorder(10,10,10,10));
    modulePanel.add(label00);

    HaxeModuleSettings settings = HaxeModuleSettings.getInstance(module);
    OpenFLTarget currentTarget = settings.getOpenFLTarget();

    addTargetPanel("iOS", HaxeIcons.Haxe_24, module, OpenFLTarget.IOS, currentTarget, modulePanel);
    addTargetPanel("Android", HaxeIcons.Haxe_24, module, OpenFLTarget.ANDROID, currentTarget, modulePanel);
    addTargetPanel("WebOS", HaxeIcons.Haxe_24, module, OpenFLTarget.WEOS, currentTarget, modulePanel);
    addTargetPanel("BlackBerry", HaxeIcons.Haxe_24, module, OpenFLTarget.BLACKBERRY, currentTarget, modulePanel);
    addTargetPanel("Windows", HaxeIcons.Haxe_24, module, OpenFLTarget.WINDOWS, currentTarget, modulePanel);
    addTargetPanel("Mac", HaxeIcons.Haxe_24, module, OpenFLTarget.MAC, currentTarget, modulePanel);
    addTargetPanel("Linux", HaxeIcons.Haxe_24, module, OpenFLTarget.LINUX, currentTarget, modulePanel);
    addTargetPanel("Linux 64", HaxeIcons.Haxe_24, module, OpenFLTarget.LINUX64, currentTarget, modulePanel);
    addTargetPanel("Flash", HaxeIcons.Haxe_24, module, OpenFLTarget.FLASH, currentTarget, modulePanel);
    addTargetPanel("Html5", HaxeIcons.Haxe_24, module, OpenFLTarget.HTML5, currentTarget, modulePanel);
    addTargetPanel("Neko", HaxeIcons.Haxe_24, module, OpenFLTarget.NEKO, currentTarget, modulePanel);
  }

  private void addTargetPanel(java.lang.String s, javax.swing.Icon icon, Module module, final OpenFLTarget target, OpenFLTarget current, JPanel modulePanel) {

    OpenFLTargetButton targetButton = new OpenFLTargetButton(s, icon, module);
    modulePanel.add(targetButton);

    targetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        OpenFLTargetButton button = (OpenFLTargetButton)e.getSource();
        HaxeModuleSettings settings = HaxeModuleSettings.getInstance(button.targetModule);
        settings.setOpenFLTarget(target);
        popup.cancel();
      }
    });

    if(current == target)
    {
      targetButton.setEnabled(false);
    }
  }
}

class OpenFLTargetButton extends JButton {

  public Module targetModule;

  public OpenFLTargetButton(java.lang.String s, javax.swing.Icon icon, Module module)
  {
    super(s,icon);
    setHorizontalAlignment(SwingConstants.LEFT);
    this.targetModule = module;
  }

}
