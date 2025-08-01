/*
  SIGNUP FORM COMPONENT - This handles faculty registration with comprehensive details
  
  INSTRUCTIONS FOR NON-CODERS:
  - To change placeholder text: Look for "placeholder=" and change the text in quotes
  - To change button text: Look for button content like "Create Account" and replace it
  - To change validation messages: Look for error handling sections
  - To modify form behavior: Look for handleSubmit function
  - To add/remove departments: Look for the DEPARTMENTS array and modify it
  - To add/remove designations: Look for the DESIGNATIONS array and modify it
*/

import React, { useState } from 'react';
import { Eye, EyeOff, User, Lock, Mail, Phone, GraduationCap, Briefcase, Calendar, IdCard } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Checkbox } from './ui/checkbox';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';

// DEPARTMENT OPTIONS - CHANGEABLE: Add or remove departments here
const DEPARTMENTS = [
  { id: 'BCA', name: 'BCA', fullName: 'Bachelor of Computer Applications' },
  { id: 'BBA', name: 'BBA', fullName: 'Bachelor of Business Administration' },
  { id: 'BCOM', name: 'BCOM', fullName: 'Bachelor of Commerce' }
];

// DESIGNATION OPTIONS - CHANGEABLE: Add or remove faculty positions here
const DESIGNATIONS = [
  { id: 'professor', name: 'Professor' },
  { id: 'associate_professor', name: 'Associate Professor' },
  { id: 'assistant_professor', name: 'Assistant Professor' },
  { id: 'lecturer', name: 'Lecturer' },
  { id: 'visiting_faculty', name: 'Visiting Faculty' },
  { id: 'guest_lecturer', name: 'Guest Lecturer' }
];

interface SignupFormProps {
  onBackToLogin: () => void; // Function to switch back to login
}

export function SignupForm({ onBackToLogin }: SignupFormProps) {
  // STATE VARIABLES - These store the current values of form fields
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  // PERSONAL INFORMATION STATES
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('');

  // PROFESSIONAL INFORMATION STATES  
  const [facultyId, setFacultyId] = useState('');
  const [selectedDepartment, setSelectedDepartment] = useState('');
  const [designation, setDesignation] = useState('');
  const [experience, setExperience] = useState('');

  // ACCOUNT INFORMATION STATES
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [agreeToTerms, setAgreeToTerms] = useState(false);

  // FORM SUBMISSION HANDLER
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    try {
      // VALIDATION CHECKS
      if (!selectedDepartment) {
        throw new Error('Please select your department.');
      }

      if (!designation) {
        throw new Error('Please select your designation.');
      }

      if (password !== confirmPassword) {
        throw new Error('Passwords do not match.');
      }

      if (password.length < 8) {
        throw new Error('Password must be at least 8 characters long.');
      }

      if (!agreeToTerms) {
        throw new Error('Please accept the terms and conditions.');
      }

      // EMAIL VALIDATION
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        throw new Error('Please enter a valid email address.');
      }

      // PHONE VALIDATION (Indian format)
      const phoneRegex = /^[6-9]\d{9}$/;
      if (!phoneRegex.test(phone)) {
        throw new Error('Please enter a valid 10-digit phone number.');
      }

      // SIMULATE API CALL - Replace with actual registration
      console.log('Signup attempt:', {
        personalInfo: { fullName, email, phone, dateOfBirth },
        professionalInfo: { facultyId, department: selectedDepartment, designation, experience },
        accountInfo: { username, password }
      });

      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 2000));

      // SUCCESS RESPONSE
      setSuccessMessage('Account created successfully! Please wait for admin approval.');
      
      // Clear form after successful submission
      setTimeout(() => {
        onBackToLogin();
      }, 3000);

    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'An error occurred during registration.');
    } finally {
      setIsLoading(false);
    }
  };

  // PASSWORD VISIBILITY TOGGLES
  const togglePasswordVisibility = () => setShowPassword(!showPassword);
  const toggleConfirmPasswordVisibility = () => setShowConfirmPassword(!showConfirmPassword);

  // DEPARTMENT SELECTION HANDLER
  const handleDepartmentSelect = (departmentId: string) => {
    setSelectedDepartment(departmentId);
    setErrorMessage('');
  };

  return (
    <Card className="w-full max-w-2xl mx-auto shadow-xl border-0 bg-card/60 backdrop-blur-md">
      {/* CARD HEADER */}
      <CardHeader className="space-y-2 text-center pb-6">
        <CardTitle className="font-display">Faculty Registration</CardTitle>
        <CardDescription className="text-muted-foreground">
          Create your account to access the seminar hall booking system
        </CardDescription>
      </CardHeader>

      {/* CARD CONTENT */}
      <CardContent className="space-y-8">
        <form onSubmit={handleSubmit} className="space-y-8">
          
          {/* SUCCESS/ERROR MESSAGES */}
          {successMessage && (
            <div className="bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-300 px-4 py-3 rounded-lg text-sm font-medium">
              {successMessage}
            </div>
          )}
          
          {errorMessage && (
            <div className="bg-destructive/10 border border-destructive/20 text-destructive px-4 py-3 rounded-lg text-sm font-medium">
              {errorMessage}
            </div>
          )}

          {/* PERSONAL INFORMATION SECTION */}
          <div className="space-y-6">
            <div className="flex items-center gap-2 mb-4">
              <User className="h-5 w-5 text-primary" />
              <h3 className="font-semibold text-lg">Personal Information</h3>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* FULL NAME */}
              <div className="space-y-2">
                <Label htmlFor="fullName" className="text-sm">Full Name *</Label>
                <Input
                  id="fullName"
                  type="text"
                  placeholder="Enter your full name"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="h-12 bg-background/50"
                  required
                  disabled={isLoading}
                />
              </div>

              {/* EMAIL */}
              <div className="space-y-2">
                <Label htmlFor="email" className="text-sm">Email Address *</Label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="email"
                    type="email"
                    placeholder="your.email@college.edu"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="pl-10 h-12 bg-background/50"
                    required
                    disabled={isLoading}
                  />
                </div>
              </div>

              {/* PHONE NUMBER */}
              <div className="space-y-2">
                <Label htmlFor="phone" className="text-sm">Phone Number *</Label>
                <div className="relative">
                  <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="phone"
                    type="tel"
                    placeholder="9876543210"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    className="pl-10 h-12 bg-background/50"
                    required
                    disabled={isLoading}
                    maxLength={10}
                  />
                </div>
              </div>

              {/* DATE OF BIRTH */}
              <div className="space-y-2">
                <Label htmlFor="dateOfBirth" className="text-sm">Date of Birth</Label>
                <div className="relative">
                  <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="dateOfBirth"
                    type="date"
                    value={dateOfBirth}
                    onChange={(e) => setDateOfBirth(e.target.value)}
                    className="pl-10 h-12 bg-background/50"
                    disabled={isLoading}
                  />
                </div>
              </div>
            </div>
          </div>

          {/* PROFESSIONAL INFORMATION SECTION */}
          <div className="space-y-6">
            <div className="flex items-center gap-2 mb-4">
              <Briefcase className="h-5 w-5 text-primary" />
              <h3 className="font-semibold text-lg">Professional Information</h3>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* FACULTY ID */}
              <div className="space-y-2">
                <Label htmlFor="facultyId" className="text-sm">Faculty ID</Label>
                <div className="relative">
                  <IdCard className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="facultyId"
                    type="text"
                    placeholder="FAC001 (if applicable)"
                    value={facultyId}
                    onChange={(e) => setFacultyId(e.target.value)}
                    className="pl-10 h-12 bg-background/50"
                    disabled={isLoading}
                  />
                </div>
              </div>

              {/* EXPERIENCE */}
              <div className="space-y-2">
                <Label htmlFor="experience" className="text-sm">Years of Experience *</Label>
                <Input
                  id="experience"
                  type="number"
                  placeholder="5"
                  value={experience}
                  onChange={(e) => setExperience(e.target.value)}
                  className="h-12 bg-background/50"
                  required
                  disabled={isLoading}
                  min="0"
                  max="50"
                />
              </div>

              {/* DEPARTMENT SELECTION */}
              <div className="space-y-2">
                <Label className="text-sm flex items-center gap-2">
                  <GraduationCap className="h-4 w-4 text-muted-foreground" />
                  Department *
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
                      title={dept.fullName}
                    >
                      <span className="font-semibold text-xs">{dept.name}</span>
                    </Button>
                  ))}
                </div>
              </div>

              {/* DESIGNATION */}
              <div className="space-y-2">
                <Label htmlFor="designation" className="text-sm">Designation *</Label>
                <Select value={designation} onValueChange={setDesignation} disabled={isLoading}>
                  <SelectTrigger className="h-12 bg-background/50">
                    <SelectValue placeholder="Select your designation" />
                  </SelectTrigger>
                  <SelectContent>
                    {DESIGNATIONS.map((desig) => (
                      <SelectItem key={desig.id} value={desig.id}>
                        {desig.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>
          </div>

          {/* ACCOUNT INFORMATION SECTION */}
          <div className="space-y-6">
            <div className="flex items-center gap-2 mb-4">
              <Lock className="h-5 w-5 text-primary" />
              <h3 className="font-semibold text-lg">Account Information</h3>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* USERNAME */}
              <div className="space-y-2 md:col-span-2">
                <Label htmlFor="username" className="text-sm">Username *</Label>
                <div className="relative">
                  <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="username"
                    type="text"
                    placeholder="Choose a unique username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="pl-10 h-12 bg-background/50"
                    required
                    disabled={isLoading}
                    minLength={4}
                  />
                </div>
              </div>

              {/* PASSWORD */}
              <div className="space-y-2">
                <Label htmlFor="password" className="text-sm">Password *</Label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="password"
                    type={showPassword ? 'text' : 'password'}
                    placeholder="Minimum 8 characters"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="pl-10 pr-10 h-12 bg-background/50"
                    required
                    disabled={isLoading}
                    minLength={8}
                  />
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

              {/* CONFIRM PASSWORD */}
              <div className="space-y-2">
                <Label htmlFor="confirmPassword" className="text-sm">Confirm Password *</Label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="confirmPassword"
                    type={showConfirmPassword ? 'text' : 'password'}
                    placeholder="Re-enter your password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="pl-10 pr-10 h-12 bg-background/50"
                    required
                    disabled={isLoading}
                  />
                  <Button
                    type="button"
                    variant="ghost"
                    size="sm"
                    className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 p-0 hover:bg-transparent"
                    onClick={toggleConfirmPasswordVisibility}
                    disabled={isLoading}
                  >
                    {showConfirmPassword ? (
                      <EyeOff className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                    ) : (
                      <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors" />
                    )}
                  </Button>
                </div>
              </div>
            </div>
          </div>

          {/* TERMS AND CONDITIONS */}
          <div className="flex items-start space-x-2">
            <Checkbox
              id="terms"
              checked={agreeToTerms}
              onCheckedChange={setAgreeToTerms}
              disabled={isLoading}
              className="mt-1"
            />
            <Label htmlFor="terms" className="text-sm cursor-pointer select-none leading-relaxed">
              I agree to the{' '}
              <Button variant="link" className="p-0 h-auto text-sm text-primary hover:text-primary/80">
                Terms and Conditions
              </Button>{' '}
              and{' '}
              <Button variant="link" className="p-0 h-auto text-sm text-primary hover:text-primary/80">
                Privacy Policy
              </Button>
            </Label>
          </div>

          {/* SUBMIT BUTTON */}
          <div className="space-y-4">
            <Button 
              type="submit" 
              className="w-full h-12 bg-primary hover:bg-primary/90 transition-all duration-200 shadow-md hover:shadow-lg font-medium"
              disabled={isLoading}
            >
              {isLoading ? (
                <div className="flex items-center gap-2">
                  <div className="h-4 w-4 animate-spin rounded-full border-2 border-primary-foreground border-t-transparent"></div>
                  Creating Account...
                </div>
              ) : (
                'Create Account'
              )}
            </Button>

            {/* BACK TO LOGIN BUTTON */}
            <Button
              type="button"
              variant="outline"
              onClick={onBackToLogin}
              className="w-full h-12 transition-all duration-200"
              disabled={isLoading}
            >
              Back to Login
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}

/*
  CUSTOMIZATION NOTES FOR NON-CODERS:
  
  1. TO CHANGE FORM FIELDS:
     - Add new fields by copying existing field structures
     - Update the state variables at the top
     - Add validation in the handleSubmit function
  
  2. TO CHANGE DEPARTMENTS:
     - Look for the DEPARTMENTS array and modify it
     - Each department needs: id, name, and fullName
  
  3. TO CHANGE DESIGNATIONS:
     - Look for the DESIGNATIONS array and modify it
     - Each designation needs: id and name
  
  4. TO MODIFY VALIDATION:
     - Look for validation checks in handleSubmit function
     - Add new validation rules as needed
  
  5. TO CHANGE TEXT:
     - Look for text in quotes and replace with your desired text
     - Update placeholder text, labels, and button text
  
  6. TO CHANGE LAYOUT:
     - Modify grid-cols-1 md:grid-cols-2 for different column layouts
     - Adjust spacing by changing space-y-6, gap-4, etc.
*/