# Funtime AutoBuy System - Complete Guide

## 🎯 Overview

The AutoBuy system is specifically designed for Funtime Minecraft servers to automatically purchase server slots using a sophisticated multi-account approach that maximizes success rates while avoiding detection.

## 🔧 Why 2 Checkers + 1 Buyer?

### The Strategy Behind Multiple Accounts

**Checker Accounts (2 accounts):**
- **Primary Function**: Continuously monitor queue status
- **Redundancy**: If one checker fails, the other continues monitoring
- **Speed**: Faster detection of available slots
- **Load Distribution**: Spreads monitoring load across multiple IPs
- **Detection Avoidance**: Rotates between accounts to avoid patterns

**Buyer Account (1 account):**
- **Dedicated Purpose**: Only used for actual purchases
- **Clean History**: Maintains clean transaction record
- **Higher Success Rate**: Specialized for purchase operations
- **Risk Management**: Reduces risk of account suspension

## 🚀 How It Works

### 1. Queue Monitoring Phase
```
┌─────────────────┐    ┌─────────────────┐
│   Checker 1     │    │   Checker 2     │
│   (Primary)     │    │   (Backup)      │
└─────────┬───────┘    └─────────┬───────┘
          │                      │
          └──────────┬───────────┘
                     │
            ┌────────▼────────┐
            │  Queue Status   │
            │   Monitoring    │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │  Decision       │
            │  Engine         │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ Slot Available? │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │   Yes/No        │
            └─────────────────┘
```

### 2. Purchase Execution Phase
```
Slot Available? ──→ Yes ──→ Buyer Account ──→ Purchase Slot
                    No ──→ Continue Monitoring
```

### 3. Anti-Detection Layer
```
┌─────────────────────────────────────────┐
│            Anti-Detection               │
├─────────────────────────────────────────┤
│ • Account Rotation                      │
│ • Randomized Timing                     │
│ • Funtime-Specific Bypasses             │
│ • Request Pattern Variation             │
│ • IP Rotation (if available)            │
└─────────────────────────────────────────┘
```

## 📋 Configuration Guide

### Basic Setup
```java
// Initialize AutoBuy
AutoBuy autobuy = new AutoBuy();

// Add checker accounts
autobuy.addCheckerAccount("checker1", "password1");
autobuy.addCheckerAccount("checker2", "password2");

// Set buyer account
autobuy.setBuyerAccount("buyer1", "password3");

// Configure timing
autobuy.setCheckInterval(3000);  // Check every 3 seconds
autobuy.setBuyDelay(500);        // 500ms delay before buying
```

### Advanced Configuration
```java
// Enable all anti-detection features
autobuy.setUseFuntimeBypass(true);
autobuy.setRandomizeTiming(true);
autobuy.setRotateAccounts(true);

// Configure server details
autobuy.setServerUrl("https://funtime.gg");
autobuy.setApiKey("your-api-key-here");

// Set timing ranges for randomization
autobuy.setMinDelay(2000);  // Minimum 2 seconds
autobuy.setMaxDelay(5000);  // Maximum 5 seconds
```

## 🛡️ Anti-Detection Features

### 1. Account Rotation System
- **Automatic Switching**: Rotates between checker accounts
- **Usage Tracking**: Monitors account usage to prevent overuse
- **Success Rate Monitoring**: Tracks success rates per account
- **Smart Selection**: Chooses best-performing accounts

### 2. Timing Randomization
- **Variable Intervals**: Random delays between requests (2-5 seconds)
- **Human-like Patterns**: Mimics real user behavior
- **Peak Hour Avoidance**: Adjusts timing based on server load
- **Burst Protection**: Prevents rapid-fire requests

### 3. Funtime-Specific Bypasses
- **Server Detection**: Specialized for Funtime server patterns
- **API Formatting**: Custom request formatting for Funtime API
- **Rate Limiting**: Intelligent rate limiting to avoid blocks
- **Error Handling**: Graceful handling of server responses

### 4. Request Pattern Variation
- **User Agent Rotation**: Varies browser signatures
- **Header Randomization**: Randomizes request headers
- **IP Rotation**: Uses different IPs when available
- **Session Management**: Maintains realistic session patterns

## 📊 Monitoring and Analytics

### Real-time Status
```java
// Check current status
boolean inQueue = autobuy.isInQueue();
int position = autobuy.getQueuePosition();
int maxSize = autobuy.getMaxQueueSize();

// Monitor account health
int failures = autobuy.getConsecutiveFailures();
long lastCheck = autobuy.getLastSuccessfulCheck();
```

### Logging System
```
[15:30:45] [INFO] Queue status - Position: 15/100
[15:30:48] [INFO] Attempting purchase with account: buyer1
[15:30:49] [INFO] Successfully purchased slot with buyer1!
[15:30:50] [INFO] AutoBuy: Account rotation completed
```

## 🔧 Advanced Features

### AutoBuyAdvanced Module
- **Multi-threaded Processing**: Parallel account management
- **Account Pools**: Organized account management
- **Advanced Rotation**: Sophisticated account switching
- **Performance Monitoring**: Detailed performance metrics

### Account Pool Management
```java
// Create account pools
autobuy.addAccount("checkers", "user1", "pass1", AccountType.CHECKER);
autobuy.addAccount("buyers", "buyer1", "pass1", AccountType.BUYER);

// Monitor pool health
Map<String, AccountPool> pools = autobuy.getAccountPools();
```

## 🚨 Troubleshooting

### Common Issues

**1. All Accounts Failing**
- Check server status
- Verify API endpoints
- Check account credentials
- Monitor for server changes

**2. Low Success Rate**
- Adjust timing intervals
- Check account health
- Verify anti-detection settings
- Monitor for detection warnings

**3. Account Suspensions**
- Reduce request frequency
- Increase randomization
- Rotate accounts more frequently
- Check for pattern detection

### Debug Mode
```java
// Enable detailed logging
autobuy.setDebugMode(true);

// Check account status
for (Account account : autobuy.getActiveAccounts()) {
    System.out.println(account.username + ": " + account.usageCount + " uses");
}

// Monitor queue health
QueueHealth health = autobuy.getQueueHealth();
System.out.println("Success rate: " + health.getSuccessRate() + "%");
```

## 📈 Best Practices

### 1. Account Management
- Use different IP addresses for each account
- Vary account creation times
- Use realistic usernames and passwords
- Regular account rotation

### 2. Timing Configuration
- Don't set intervals too low (risk of detection)
- Use randomized timing
- Monitor success rates and adjust
- Avoid peak hours when possible

### 3. Server Selection
- Choose servers with higher success rates
- Monitor server capacity
- Use multiple server endpoints if available
- Track server performance

### 4. Security
- Store credentials securely
- Use environment variables
- Implement rate limiting
- Monitor for suspicious activity

## ⚖️ Legal and Ethical Considerations

### Terms of Service
- Review Funtime server ToS
- Ensure compliance with server rules
- Respect rate limits and usage policies
- Monitor for policy changes

### Responsible Usage
- Use reasonable request frequencies
- Don't overload servers
- Respect other users
- Monitor for abuse

### Risk Management
- Use dedicated accounts
- Monitor for suspensions
- Have backup strategies
- Regular account rotation

## 🔮 Future Enhancements

### Planned Features
- Machine learning for optimal timing
- Advanced pattern recognition
- Multi-server support
- Real-time analytics dashboard
- Cloud-based account management

### Integration Options
- Discord notifications
- Web dashboard
- Mobile app integration
- API for third-party tools

## 📞 Support

For issues, questions, or feature requests:
- Check the troubleshooting guide
- Review logs for error messages
- Test with single account first
- Verify server status

---

**Disclaimer**: This system is for educational purposes only. Users are responsible for complying with server terms of service and applicable laws. The developers are not responsible for any consequences of using this software.