# Node.js Module Corruption Troubleshooting Guide

## Overview
This guide provides step-by-step instructions for diagnosing and fixing Node.js module corruption issues, particularly with `react-scripts` and other dependencies in React applications.

## Common Symptoms of Node Module Corruption

### 1. Module Not Found Errors
```
Error: Cannot find module '../scripts/start'
Error: Cannot find module 'react-scripts'
Error: Cannot find module 'webpack'
```

### 2. Permission Errors
```
EACCES: permission denied, access '/path/to/node_modules'
```

### 3. Corrupted Binary Files
```
Error: spawn ENOENT
Error: Command failed with exit code 1
```

### 4. Inconsistent Dependencies
```
npm ERR! peer dep missing
npm ERR! invalid package-lock.json
```

## Step-by-Step Troubleshooting Process

### Step 1: Identify the Issue

#### Check if the process is running
```bash
# Check if any Node.js processes are running
ps aux | grep node

# Check if the port is in use
lsof -i :3000  # For React default port
lsof -i :8080  # For other common ports
```

#### Check package.json integrity
```bash
# Navigate to your project directory
cd /path/to/your/project

# Verify package.json exists and is valid JSON
cat package.json | jq .  # If jq is installed
# OR
node -e "console.log(JSON.parse(require('fs').readFileSync('package.json', 'utf8')))"
```

#### Check node_modules structure
```bash
# Check if node_modules exists
ls -la node_modules/

# Check if react-scripts binary exists
ls -la node_modules/.bin/ | grep react-scripts

# Check if the binary is executable
ls -la node_modules/.bin/react-scripts
```

### Step 2: Kill Running Processes

#### Kill Node.js processes
```bash
# Find Node.js processes
ps aux | grep node

# Kill specific process by PID
kill -9 <PID>

# Kill all Node.js processes (use with caution)
pkill -f node
```

#### Kill processes using specific ports
```bash
# Find process using port 3000
lsof -i :3000

# Kill process using port 3000
kill -9 $(lsof -t -i:3000)

# For other ports, replace 3000 with your port number
```

### Step 3: Clean Corrupted Dependencies

#### Method 1: Complete Clean (Recommended)
```bash
# Navigate to your project directory
cd /path/to/your/project

# Remove node_modules directory
rm -rf node_modules

# Remove package-lock.json
rm -f package-lock.json

# Remove yarn.lock if using Yarn
rm -f yarn.lock

# Clear npm cache
npm cache clean --force

# Clear npm cache for specific package
npm cache clean --force --prefix /path/to/your/project
```

#### Method 2: Selective Clean
```bash
# Remove specific corrupted package
rm -rf node_modules/react-scripts

# Reinstall specific package
npm install react-scripts@5.0.1
```

### Step 4: Reinstall Dependencies

#### Using npm
```bash
# Install dependencies
npm install

# If you encounter permission issues, use sudo (not recommended for production)
sudo npm install

# Install with verbose output for debugging
npm install --verbose

# Install with legacy peer deps (if encountering peer dependency issues)
npm install --legacy-peer-deps
```

#### Using Yarn (Alternative)
```bash
# Install Yarn if not already installed
npm install -g yarn

# Install dependencies
yarn install

# Clear Yarn cache if needed
yarn cache clean
```

### Step 5: Verify Installation

#### Check installation success
```bash
# Verify node_modules structure
ls -la node_modules/

# Check if react-scripts is properly installed
ls -la node_modules/.bin/react-scripts

# Test if the binary works
./node_modules/.bin/react-scripts --version
```

#### Test the application
```bash
# Start the development server
npm start

# Or using yarn
yarn start
```

## Advanced Troubleshooting

### Check Node.js and npm Versions
```bash
# Check Node.js version
node --version

# Check npm version
npm --version

# Check if versions are compatible
npm doctor
```

### Clear All Caches
```bash
# Clear npm cache
npm cache clean --force

# Clear Yarn cache
yarn cache clean

# Clear system cache (macOS)
sudo dscacheutil -flushcache
sudo killall -HUP mDNSResponder
```

### Check File Permissions
```bash
# Check permissions on node_modules
ls -la node_modules/

# Fix permissions if needed
sudo chown -R $(whoami) node_modules/
sudo chmod -R 755 node_modules/
```

### Environment Variables
```bash
# Check npm configuration
npm config list

# Check environment variables
echo $NODE_PATH
echo $NPM_CONFIG_PREFIX

# Reset npm configuration
npm config delete prefix
```

## Prevention Tips

### 1. Use Version Managers
```bash
# Install nvm (Node Version Manager)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Use specific Node.js version
nvm install 18.16.1
nvm use 18.16.1
```

### 2. Lock Dependencies
```bash
# Always commit package-lock.json
git add package-lock.json
git commit -m "Lock dependencies"

# Use exact versions in package.json for critical dependencies
```

### 3. Regular Maintenance
```bash
# Update dependencies regularly
npm update

# Check for security vulnerabilities
npm audit

# Fix vulnerabilities
npm audit fix
```

### 4. Use .nvmrc File
```bash
# Create .nvmrc file with Node.js version
echo "18.16.1" > .nvmrc

# Use the version specified in .nvmrc
nvm use
```

## Common Error Solutions

### Error: "Cannot find module '../scripts/start'"
**Solution:**
```bash
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Error: "EACCES: permission denied"
**Solution:**
```bash
sudo chown -R $(whoami) ~/.npm
sudo chown -R $(whoami) /usr/local/lib/node_modules
```

### Error: "spawn ENOENT"
**Solution:**
```bash
rm -rf node_modules
npm install
```

### Error: "Invalid package-lock.json"
**Solution:**
```bash
rm package-lock.json
npm install
```

## Quick Reference Commands

### Complete Reset (Nuclear Option)
```bash
# Kill all Node processes
pkill -f node

# Remove all traces
rm -rf node_modules package-lock.json yarn.lock

# Clear all caches
npm cache clean --force
yarn cache clean

# Reinstall everything
npm install

# Start application
npm start
```

### Check System Health
```bash
# Check Node.js health
npm doctor

# Check for outdated packages
npm outdated

# Check for security issues
npm audit
```

## File Structure After Clean Install

After a successful clean install, your project should have:
```
project/
├── node_modules/
│   ├── .bin/
│   │   ├── react-scripts
│   │   └── ... (other binaries)
│   ├── react-scripts/
│   └── ... (other dependencies)
├── package.json
├── package-lock.json
└── src/
```

## When to Contact Support

Contact your development team or system administrator if:
- The issue persists after following all steps
- You encounter permission errors that can't be resolved
- The application works on other machines but not yours
- You see unusual error messages not covered in this guide

---

**Last Updated:** September 2024  
**Version:** 1.0  
**Author:** JV Constructions Development Team
