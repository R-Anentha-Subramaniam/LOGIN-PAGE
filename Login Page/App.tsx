/*
  MAIN APPLICATION COMPONENT - This is the entry point of the application
  
  INSTRUCTIONS FOR NON-CODERS:
  - To change the main title: Look for "RNS First Grade College" and replace it
  - To change the subtitle: Look for "Seminar Hall Booking System" and replace it
  - To change the logo: Replace the logo import with your new logo file
  - To modify layout: Look for className attributes and spacing values
*/

import { useState } from 'react';
import { ThemeProvider } from './components/ThemeProvider';     // Handles light/dark mode switching
import { ThemeToggle } from './components/ThemeToggle';         // Theme toggle button component
import { LoginForm } from './components/LoginForm';             // Login form component
import { SignupForm } from './components/SignupForm';           // Signup form component
import { Button } from './components/ui/button';                // Button component
import collegeLogos from 'figma:asset/f0aa1e36cb7052af5d93acb1d4ed1337b738d2de.png'; // College logo image

export default function App() {
  // STATE MANAGEMENT - Controls which form to show
  const [currentView, setCurrentView] = useState<'login' | 'signup'>('login');

  // NAVIGATION HANDLERS - Switch between login and signup
  const showLoginForm = () => setCurrentView('login');
  const showSignupForm = () => setCurrentView('signup');

  return (
    <ThemeProvider>
      {/* MAIN CONTAINER - Full screen background with enhanced gradient */}
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-primary/8 flex items-center justify-center p-6 relative">
        
        {/* THEME TOGGLE BUTTON - Positioned in top right corner */}
        <div className="absolute top-6 right-6">
          <ThemeToggle />
        </div>

        {/* MAIN CONTENT CONTAINER - Centers all content */}
        <div className="w-full max-w-2xl">
          
          {/* HEADER SECTION - Logo and college information */}
          <div className="text-center mb-10">
            
            {/* COLLEGE LOGO - Displays the imported logo image */}
            <div className="flex justify-center mb-8">
              <div className="relative">
                <img 
                  src={collegeLogos}
                  alt="RNS First Grade College Logo"           // CHANGEABLE: Update alt text for accessibility
                  className="w-28 h-28 object-contain drop-shadow-2xl transition-transform duration-300 hover:scale-105" // CHANGEABLE: Adjust w-28 h-28 to change logo size
                />
                {/* SUBTLE BACKGROUND GLOW FOR LOGO */}
                <div className="absolute inset-0 bg-primary/5 rounded-full -z-10 blur-xl scale-150"></div>
              </div>
            </div>

            {/* COLLEGE NAME - Main heading with elegant typography */}
            <h1 className="font-display text-4xl tracking-tight text-foreground mb-3">
              RNS First Grade College                        {/* CHANGEABLE: Replace with your college name */}
            </h1>
            
            {/* SYSTEM DESCRIPTION - Subtitle with improved spacing */}
            <h2 className="text-xl text-muted-foreground mb-6 font-medium">
              Seminar Hall Booking System                    {/* CHANGEABLE: Replace with your system description */}
            </h2>

            {/* CONDITIONAL WELCOME MESSAGE - Different message for login vs signup */}
            <div className="bg-primary/5 border border-primary/10 rounded-lg p-4 mb-2">
              {currentView === 'login' ? (
                <p className="text-sm text-muted-foreground leading-relaxed">
                  Faculty members, please select your department and log in to access the booking system
                  {/* CHANGEABLE: Replace with your custom welcome message for login */}
                </p>
              ) : (
                <p className="text-sm text-muted-foreground leading-relaxed">
                  New faculty members, please fill out the registration form to create your account
                  {/* CHANGEABLE: Replace with your custom welcome message for signup */}
                </p>
              )}
            </div>

            {/* CONDITIONAL QUICK INSTRUCTIONS */}
            {currentView === 'login' ? (
              <p className="text-xs text-muted-foreground/80 mt-3">
                Select your department • Enter credentials • Access dashboard
              </p>
            ) : (
              <p className="text-xs text-muted-foreground/80 mt-3">
                Fill personal details • Add professional info • Create account
              </p>
            )}
          </div>

          {/* CONDITIONAL FORM RENDERING - Show login or signup form based on currentView */}
          {currentView === 'login' ? (
            /* LOGIN FORM COMPONENT */
            <LoginForm onSwitchToSignup={showSignupForm} />
          ) : (
            /* SIGNUP FORM COMPONENT */
            <SignupForm onBackToLogin={showLoginForm} />
          )}

          {/* FOOTER SECTION - Copyright information */}
          <div className="text-center mt-8">
            <div className="w-full h-px bg-gradient-to-r from-transparent via-border to-transparent mb-6"></div>
            <p className="text-sm text-muted-foreground">
              © 2024 RNS First Grade College. All rights reserved.
              {/* CHANGEABLE: Update copyright year and college name */}
            </p>
          </div>
        </div>
      </div>
    </ThemeProvider>
  );
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. CHANGING THE LOGO:
     - Replace the logo file in your project
     - Update the import statement at the top: import collegeLogos from 'path/to/your/logo.png';
     - Adjust logo size by changing w-28 h-28 to different values (w-24 h-24 for smaller, w-32 h-32 for larger)
  
  2. CHANGING TEXT CONTENT:
     - College name: Replace "RNS First Grade College"
     - System name: Replace "Seminar Hall Booking System" 
     - Welcome messages: Replace the text in the instruction boxes for both login and signup
     - Copyright: Update the year and college name in the footer
  
  3. CHANGING LAYOUT:
     - Logo size: Modify w-28 h-28 classes
     - Spacing: Look for mb-8, mt-8, etc. and change numbers
     - Text size: Look for text-4xl, text-xl, etc. and modify
     - Form width: Change max-w-2xl to max-w-md for narrower forms or max-w-4xl for wider forms
  
  4. CHANGING COLORS:
     - Colors are controlled in styles/globals.css
     - Look for --primary, --background, etc. and change the color values
  
  5. ADDING MORE CONTENT:
     - Add new sections by copying existing div structures
     - Place them before or after the form components
     - Follow the same className patterns for consistent styling
  
  6. TYPOGRAPHY CLASSES:
     - font-display: Uses elegant serif font for headings
     - font-body: Uses clean sans-serif font for body text
     - Add these classes to any element for consistent typography
  
  7. NAVIGATION BEHAVIOR:
     - The currentView state controls which form is shown
     - showLoginForm() and showSignupForm() functions switch between forms
     - You can add more views by extending the currentView type and adding more conditions
*/