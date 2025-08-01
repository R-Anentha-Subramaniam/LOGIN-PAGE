/*
  THEME MANAGER CLASS - Handles light/dark mode switching
  
  INSTRUCTIONS FOR NON-CODERS:
  - This class manages the application's color theme (light/dark)
  - To add new themes: Add new enum values in Theme enum
  - To change default theme: Modify DEFAULT_THEME constant
  - Theme preferences are automatically saved to user's system
*/

package com.college.booking;

import javafx.scene.Scene;
import java.util.prefs.Preferences;

/**
 * THEME MANAGER CLASS
 * Manages application themes and provides easy switching between light/dark modes
 */
public class ThemeManager {
    
    // THEME ENUMERATION - Add new themes here
    public enum Theme {
        LIGHT("Light", "/css/light-theme.css"),
        DARK("Dark", "/css/dark-theme.css");
        
        private final String displayName;
        private final String cssFile;
        
        Theme(String displayName, String cssFile) {
            this.displayName = displayName;
            this.cssFile = cssFile;
        }
        
        public String getDisplayName() { return displayName; }
        public String getCssFile() { return cssFile; }
    }
    
    // CONSTANTS - CHANGEABLE: Modify default theme here
    private static final Theme DEFAULT_THEME = Theme.LIGHT;
    private static final String THEME_PREFERENCE_KEY = "selectedTheme";
    
    // INSTANCE VARIABLES
    private Theme currentTheme;
    private final Preferences preferences;
    
    /**
     * CONSTRUCTOR - Initialize theme manager
     */
    public ThemeManager() {
        // GET USER PREFERENCES
        preferences = Preferences.userNodeForPackage(ThemeManager.class);
        
        // LOAD SAVED THEME OR USE DEFAULT
        String savedTheme = preferences.get(THEME_PREFERENCE_KEY, DEFAULT_THEME.name());
        try {
            currentTheme = Theme.valueOf(savedTheme);
        } catch (IllegalArgumentException e) {
            currentTheme = DEFAULT_THEME;
            System.out.println("⚠️ Invalid theme preference, using default: " + DEFAULT_THEME.getDisplayName());
        }
        
        System.out.println("🎨 Theme Manager initialized with theme: " + currentTheme.getDisplayName());
    }
    
    /**
     * APPLY THEME - Apply the specified theme to a scene
     * 
     * @param scene The JavaFX scene to apply theme to
     * @param theme The theme to apply
     */
    public void applyTheme(Scene scene, Theme theme) {
        if (scene == null) {
            System.err.println("❌ Cannot apply theme: Scene is null");
            return;
        }
        
        try {
            // CLEAR EXISTING STYLESHEETS
            scene.getStylesheets().clear();
            
            // ADD BASE STYLESHEET (common styles)
            scene.getStylesheets().add(getClass().getResource("/css/base-styles.css").toExternalForm());
            
            // ADD THEME-SPECIFIC STYLESHEET
            scene.getStylesheets().add(getClass().getResource(theme.getCssFile()).toExternalForm());
            
            // UPDATE CURRENT THEME
            currentTheme = theme;
            
            System.out.println("✅ Applied theme: " + theme.getDisplayName());
            
        } catch (Exception e) {
            System.err.println("❌ Error applying theme: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * TOGGLE THEME - Switch between light and dark themes
     * 
     * @param scene The scene to apply the new theme to
     */
    public void toggleTheme(Scene scene) {
        Theme newTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        applyTheme(scene, newTheme);
        
        System.out.println("🔄 Theme toggled to: " + newTheme.getDisplayName());
    }
    
    /**
     * SET THEME - Set a specific theme
     * 
     * @param scene The scene to apply the theme to
     * @param theme The theme to set
     */
    public void setTheme(Scene scene, Theme theme) {
        if (theme != currentTheme) {
            applyTheme(scene, theme);
        }
    }
    
    /**
     * SAVE THEME PREFERENCE - Save current theme to user preferences
     */
    public void saveThemePreference() {
        try {
            preferences.put(THEME_PREFERENCE_KEY, currentTheme.name());
            preferences.flush();
            System.out.println("💾 Theme preference saved: " + currentTheme.getDisplayName());
        } catch (Exception e) {
            System.err.println("❌ Error saving theme preference: " + e.getMessage());
        }
    }
    
    /**
     * GETTER METHODS
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }
    
    public boolean isDarkMode() {
        return currentTheme == Theme.DARK;
    }
    
    public boolean isLightMode() {
        return currentTheme == Theme.LIGHT;
    }
    
    /**
     * GET ALL THEMES - Return array of all available themes
     */
    public Theme[] getAllThemes() {
        return Theme.values();
    }
    
    /**
     * AUTO-DETECT SYSTEM THEME - Try to detect system preference (Windows 10+, macOS, Linux)
     * This is a placeholder - real implementation would need platform-specific code
     */
    public Theme detectSystemTheme() {
        // PLACEHOLDER IMPLEMENTATION
        // In a real application, you would check:
        // - Windows: Registry key for dark mode
        // - macOS: System preferences
        // - Linux: Desktop environment settings
        
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            
            // FOR NOW, RETURN DEFAULT THEME
            // TODO: Implement actual system theme detection
            System.out.println("🔍 System theme detection not yet implemented for: " + osName);
            return DEFAULT_THEME;
            
        } catch (Exception e) {
            System.err.println("❌ Error detecting system theme: " + e.getMessage());
            return DEFAULT_THEME;
        }
    }
    
    /**
     * APPLY THEME BY NAME - Apply theme using string name
     * 
     * @param scene Scene to apply theme to
     * @param themeName Name of the theme (case-insensitive)
     */
    public void applyThemeByName(Scene scene, String themeName) {
        try {
            Theme theme = Theme.valueOf(themeName.toUpperCase());
            applyTheme(scene, theme);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Unknown theme: " + themeName + ". Available themes: ");
            for (Theme t : Theme.values()) {
                System.err.println("   - " + t.name().toLowerCase());
            }
        }
    }
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. ADDING NEW THEMES:
     - Add new entries to the Theme enum
     - Create corresponding CSS files in src/main/resources/css/
     - Example: BLUE("Blue", "/css/blue-theme.css")
  
  2. CHANGING DEFAULT THEME:
     - Modify the DEFAULT_THEME constant
     - Users can still override this with their preference
  
  3. THEME PERSISTENCE:
     - Themes are automatically saved to user preferences
     - Preferences are stored per-user, per-system
     - No additional setup required
  
  4. CSS FILE ORGANIZATION:
     - base-styles.css: Common styles for all themes
     - light-theme.css: Light theme specific colors
     - dark-theme.css: Dark theme specific colors
  
  5. SYSTEM THEME DETECTION:
     - Currently a placeholder implementation
     - Can be extended to detect OS dark/light mode
     - Would require platform-specific code
  
  6. ERROR HANDLING:
     - Invalid theme names fall back to default
     - Missing CSS files are logged but don't crash app
     - User preferences are validated on load
*/