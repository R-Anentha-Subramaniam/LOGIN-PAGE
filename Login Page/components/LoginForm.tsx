/*
  LOGIN FORM COMPONENT - This handles the faculty login interface with department selection
  
  INSTRUCTIONS FOR NON-CODERS:
  - To change placeholder text: Look for "placeholder=" and change the text in quotes
  - To change button text: Look for button content like "Sign In" and replace it
  - To change validation messages: Look for error handling sections
  - To modify form behavior: Look for handleSubmit function
  - To add/remove departments: Look for the departments array and modify it
*/

import React, { useState, useEffect } from 'react';
import { Eye, EyeOff, User, Lock, GraduationCap } from 'lucide-react';        // Icons for UI elements
import { Button } from './ui/button';                          // Pre-built button component
import { Input } from './ui/input';                            // Pre-built input component
import { Label } from './ui/label';                            // Pre-built label component
import { Checkbox } from './ui/checkbox';                      // Pre-built checkbox component
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card'; // Pre-built card components

// DEPARTMENT OPTIONS - CHANGEABLE: Add or remove departments here
const DEPARTMENTS = [
  { id: 'BCA', name: 'BCA', fullName: 'Bachelor of Computer Applications' },
  { id: 'BBA', name: 'BBA', fullName: 'Bachelor of Business Administration' },
  { id: 'BCOM', name: 'BCOM', fullName: 'Bachelor of Commerce' }
];

interface LoginFormProps {
  onSwitchToSignup?: () => void; // Optional function to switch to signup
}

export function LoginForm({ onSwitchToSignup }: LoginFormProps = {}) {
  // STATE VARIABLES - These store the current values of form fields
  const [showPassword, setShowPassword] = useState(false);     // Controls password visibility
  const [username, setUsername] = useState('');               // Stores username input
  const [password, setPassword] = useState('');               // Stores password input
  const [selectedDepartment, setSelectedDepartment] = useState(''); // Stores selected department
  const [rememberMe, setRememberMe] = useState(false);        // Stores remember me checkbox state
  const [isLoading, setIsLoading] = useState(false);          // Controls loading state during login
  const [errorMessage, setErrorMessage] = useState('');       // Stores error messages

  // LOAD SAVED CREDENTIALS - This runs when the component first loads
  useEffect(() => {
    // Check if user previously selected "Remember Me"
    const savedUsername = localStorage.getItem('rememberedUsername');
    const savedDepartment = localStorage.getItem('rememberedDepartment');
    const wasRemembered = localStorage.getItem('rememberMe') === 'true';
    
    if (savedUsername && wasRemembered) {
      setUsername(savedUsername);      // Fill in the saved username
      setRememberMe(true);            // Check the remember me box
      if (savedDepartment) {
        setSelectedDepartment(savedDepartment); // Fill in the saved department
      }
    }
  }, []);

  // FORM SUBMISSION HANDLER - This function runs when user clicks "Sign In"
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();               // Prevent page reload
    setIsLoading(true);              // Show loading state
    setErrorMessage('');             // Clear any previous errors

    // VALIDATE DEPARTMENT SELECTION
    if (!selectedDepartment) {
      setErrorMessage('Please select your department.');
      setIsLoading(false);
      return;
    }

    try {
      // SIMULATE LOGIN PROCESS - Replace this with actual authentication
      console.log('Login attempt:', { 
        username, 
        password, 
        department: selectedDepartment,
        rememberMe 
      });
      
      // Simulate API call delay (remove in production)
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // HANDLE REMEMBER ME FUNCTIONALITY
      if (rememberMe) {
        // Save username, department, and remember preference to browser storage
        localStorage.setItem('rememberedUsername', username);
        localStorage.setItem('rememberedDepartment', selectedDepartment);
        localStorage.setItem('rememberMe', 'true');
      } else {
        // Clear saved credentials if remember me is unchecked
        localStorage.removeItem('rememberedUsername');
        localStorage.removeItem('rememberedDepartment');
        localStorage.removeItem('rememberMe');
      }

      // SUCCESS - Redirect to dashboard (replace with actual navigation)
      alert(`Login successful! Welcome ${username} from ${selectedDepartment} department. Redirecting to dashboard...`);
      
    } catch (error) {
      // ERROR HANDLING - Show error message to user
      setErrorMessage('Invalid credentials. Please check your username, password, and department.');
      console.error('Login error:', error);
    } finally {
      setIsLoading(false);           // Hide loading state
    }
  };

  // PASSWORD VISIBILITY TOGGLE - Shows/hides password characters
  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  // FORGOT PASSWORD HANDLER - Opens forgot password dialog/page
  const handleForgotPassword = () => {
    // TODO: Implement forgot password functionality
    alert('Forgot password functionality will be implemented here');
  };

  // DEPARTMENT SELECTION HANDLER - Updates selected department
  const handleDepartmentSelect = (departmentId: string) => {
    setSelectedDepartment(departmentId);
    setErrorMessage(''); // Clear any error when department is selected
  };

  return (
    <Card className="w-full max-w-md mx-auto shadow-xl border-0 bg-card/60 backdrop-blur-md">
      {/* CARD HEADER - Title and description section */}
      <CardHeader className="space-y-2 text-center pb-6">
        <CardTitle className="font-display">Faculty Login</CardTitle>
        <CardDescription className="text-muted-foreground">
          Access the seminar hall booking system
        </CardDescription>
      </CardHeader>

      {/* CARD CONTENT - Main form section */}
      <CardContent className="space-y-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          
          {/* ERROR MESSAGE DISPLAY - Shows login errors */}
          {errorMessage && (
            <div className="bg-destructive/10 border border-destructive/20 text-destructive px-4 py-3 rounded-lg text-sm font-medium">
              {errorMessage}
            </div>
          )}

          {/* DEPARTMENT SELECTION - Choose faculty department */}
          <div className="space-y-3">
            <Label className="text-sm flex items-center gap-2">
              <GraduationCap className="h-4 w-4 text-muted-foreground" />
              Select Your Department
            </Label>
            <div className="grid grid-cols-3 gap-2">
              {DEPARTMENTS.map((dept) => (
                <Button
                  key={dept.id}
                  type="button"
                  variant={selectedDepartment === dept.id ? "default" : "outline"}
                  size="sm"
                  onClick={() => handleDepartmentSelect(dept.id)}
                  disabled={isLoading}
                  className={`
                    h-12 flex flex-col items-center justify-center space-y-1 transition-all duration-200
                    ${selectedDepartment === dept.id 
                      ? 'bg-primary text-primary-foreground shadow-md scale-105' 
                      : 'hover:bg-accent hover:text-accent-foreground hover:scale-102'
                    }
                  `}
                  title={dept.fullName} // Tooltip showing full department name
                >
                  <span className="font-semibold text-xs">
                    {dept.name}
                  </span>
                </Button>
              ))}
            </div>
            {/* DEPARTMENT FULL NAME DISPLAY - Shows full name of selected department */}
            {selectedDepartment && (
              <p className="text-xs text-muted-foreground text-center">
                {DEPARTMENTS.find(d => d.id === selectedDepartment)?.fullName}
              </p>
            )}
          </div>

          {/* USERNAME INPUT FIELD */}
          <div className="space-y-2">
            <Label htmlFor="username" className="text-sm">Faculty Username</Label>
            <div className="relative">
              {/* User icon inside input field */}
              <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="username"
                type="text"
                placeholder="Enter your faculty username"    // CHANGEABLE: Modify placeholder text here
                value={username}
                onChange={(e) => setUsername(e.target.value)} // Updates username state when user types
                className="pl-10 h-12 border-border/50 focus:border-primary transition-all duration-200 bg-background/50"
                required                                      // Makes field mandatory
                disabled={isLoading}                         // Disable during login process
              />
            </div>
          </div>

          {/* PASSWORD INPUT FIELD */}
          <div className="space-y-2">
            <Label htmlFor="password" className="text-sm">Password</Label>
            <div className="relative">
              {/* Lock icon inside input field */}
              <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="password"
                type={showPassword ? 'text' : 'password'}    // Toggle between text and password type
                placeholder="Enter your password"            // CHANGEABLE: Modify placeholder text here
                value={password}
                onChange={(e) => setPassword(e.target.value)} // Updates password state when user types
                className="pl-10 pr-10 h-12 border-border/50 focus:border-primary transition-all duration-200 bg-background/50"
                required                                      // Makes field mandatory
                disabled={isLoading}                         // Disable during login process
              />
              {/* PASSWORD VISIBILITY TOGGLE BUTTON */}
              <Button
                type="button"
                variant="ghost"
                size="sm"
                className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 p-0 hover:bg-transparent"
                onClick={togglePasswordVisibility}
                disabled={isLoading}
              >
                {showPassword ? (
                  <EyeOff className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                ) : (
                  <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                )}
              </Button>
            </div>
          </div>

          {/* REMEMBER ME CHECKBOX AND FORGOT PASSWORD LINK */}
          <div className="flex items-center justify-between">
            {/* REMEMBER ME CHECKBOX */}
            <div className="flex items-center space-x-2">
              <Checkbox
                id="remember"
                checked={rememberMe}
                onCheckedChange={setRememberMe}               // Updates remember me state
                disabled={isLoading}                         // Disable during login process
              />
              <Label 
                htmlFor="remember" 
                className="text-sm cursor-pointer select-none"
              >
                Remember me                                   {/* CHANGEABLE: Modify checkbox label here */}
              </Label>
            </div>

            {/* FORGOT PASSWORD LINK */}
            <Button
              type="button"
              variant="link"
              onClick={handleForgotPassword}
              className="p-0 h-auto text-sm text-primary hover:text-primary/80 transition-colors"
              disabled={isLoading}
            >
              Forgot password?                              {/* CHANGEABLE: Modify link text here */}
            </Button>
          </div>

          {/* SIGN IN BUTTON */}
          <Button 
            type="submit" 
            className="w-full h-12 bg-primary hover:bg-primary/90 transition-all duration-200 shadow-md hover:shadow-lg font-medium"
            disabled={isLoading || !username || !password || !selectedDepartment}  // Disable if loading or fields empty
          >
            {isLoading ? (
              <div className="flex items-center gap-2">
                <div className="h-4 w-4 animate-spin rounded-full border-2 border-primary-foreground border-t-transparent"></div>
                Signing In...
              </div>
            ) : (
              'Sign In'                                      // CHANGEABLE: Modify button text here
            )}
          </Button>
        </form>

        {/* SIGNUP LINK - Only show if onSwitchToSignup function is provided */}
        {onSwitchToSignup && (
          <div className="text-center mt-6 pt-6 border-t border-border/50">
            <p className="text-sm text-muted-foreground">
              New faculty member?{' '}
              <Button
                variant="link"
                onClick={onSwitchToSignup}
                className="p-0 h-auto text-sm text-primary hover:text-primary/80 transition-colors"
                disabled={isLoading}
              >
                Create an account
              </Button>
            </p>
          </div>
        )}
      </CardContent>
    </Card>
  );
}

/*
  CUSTOMIZATION NOTES FOR NON-CODERS:
  
  1. TO CHANGE DEPARTMENTS:
     - Look for the DEPARTMENTS array at the top
     - Add/remove/modify departments using the same format
     - Each department needs: id, name, and fullName
  
  2. TO CHANGE TEXT:
     - Look for text in quotes like "Sign In" or "Enter your username"
     - Replace with your desired text
  
  3. TO CHANGE COLORS:
     - The colors are controlled by the globals.css file
     - Look for --primary, --background, etc. and change the color values
  
  4. TO CHANGE BEHAVIOR:
     - The handleSubmit function controls what happens when login is attempted
     - The useEffect at the top controls what happens when the page loads
  
  5. TO ADD MORE VALIDATION:
     - Add checks in the handleSubmit function before the try block
     - Set error messages using setErrorMessage()
  
  6. TO MODIFY DEPARTMENT LAYOUT:
     - Change "grid-cols-3" to "grid-cols-2" for 2 columns or "grid-cols-1" for single column
     - Adjust button sizing by changing "h-12" to different values
*/