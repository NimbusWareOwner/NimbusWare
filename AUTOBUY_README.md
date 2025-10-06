# AutoBuy System for Funtime Servers

## Overview

The AutoBuy system is designed to automatically purchase slots on Funtime Minecraft servers using multiple accounts to maximize success rates and avoid detection.

## Why Multiple Accounts?

### 2 Checker Accounts + 1 Buyer Account System

**Checker Accounts (2 accounts):**
- **Purpose**: Monitor queue status and server availability
- **Function**: Continuously check if slots are available
- **Benefits**: 
  - Redundancy - if one checker fails, the other continues
  - Faster detection of available slots
  - Reduced risk of detection through account rotation
  - Load distribution across multiple IPs

**Buyer Account (1 account):**
- **Purpose**: Actually purchase the slot when available
- **Function**: Execute the purchase transaction
- **Benefits**:
  - Dedicated account for purchases (cleaner transaction history)
  - Reduced risk of account suspension
  - Better success rates for actual purchases

## How It Works

### 1. Queue Monitoring
```
Checker Account 1 ──┐
                   ├──→ Queue Status Check ──→ Decision Engine
Checker Account 2 ──┘
```

### 2. Purchase Process
```
Queue Available? ──→ Yes ──→ Buyer Account ──→ Purchase Slot
                    No ──→ Continue Monitoring
```

### 3. Anti-Detection Features
- **Account Rotation**: Switches between accounts to avoid patterns
- **Randomized Timing**: Varies check intervals to appear human-like
- **Funtime Bypass**: Specialized anti-detection for Funtime servers
- **Request Spacing**: Delays between requests to avoid rate limiting

## Configuration

### Basic Setup
```java
// Add checker accounts
autobuy.addAccount("checkers", "checker1", "password1", AccountType.CHECKER);
autobuy.addAccount("checkers", "checker2", "password2", AccountType.CHECKER);

// Add buyer account
autobuy.addAccount("buyers", "buyer1", "password3", AccountType.BUYER);

// Configure timing
autobuy.setCheckInterval(3000); // Check every 3 seconds
autobuy.setBuyDelay(500);       // 500ms delay before buying
```

### Advanced Configuration
```java
// Enable anti-detection
autobuy.setUseFuntimeBypass(true);
autobuy.setRandomizeTiming(true);
autobuy.setRotateAccounts(true);

// Set server details
autobuy.setServerUrl("https://funtime.gg");
autobuy.setApiKey("your-api-key");
```

## Account Management

### Account Pools
- **Checker Pool**: Contains all accounts used for monitoring
- **Buyer Pool**: Contains all accounts used for purchasing
- **Automatic Rotation**: Accounts are rotated to distribute usage

### Usage Tracking
- Each account tracks usage count
- Success rates decrease with usage (simulates real behavior)
- Accounts are rotated based on usage patterns

## Anti-Detection Strategies

### 1. Timing Randomization
- Random delays between requests (100-500ms)
- Variable check intervals (3-7 seconds)
- Human-like request patterns

### 2. Account Rotation
- Switches between checker accounts
- Rotates buyer accounts
- Prevents single account overuse

### 3. Request Patterns
- Varies user agents
- Randomizes request headers
- Mimics real browser behavior

### 4. Funtime-Specific Bypasses
- Specialized for Funtime server detection
- Custom request formatting
- Server-specific timing patterns

## Monitoring and Logging

### Queue Status
- Real-time queue position tracking
- Server availability monitoring
- Success/failure rate tracking

### Account Health
- Usage count per account
- Success rate per account
- Error tracking and recovery

### Logging
```
[15:30:45] [INFO] Queue status - Position: 15/100
[15:30:48] [INFO] Attempting purchase with account: buyer1
[15:30:49] [INFO] Successfully purchased slot with buyer1!
```

## Best Practices

### 1. Account Setup
- Use different IP addresses for each account
- Vary account creation times
- Use realistic usernames and passwords

### 2. Timing Configuration
- Don't set intervals too low (risk of detection)
- Use randomized timing
- Monitor success rates and adjust

### 3. Server Selection
- Choose servers with higher success rates
- Monitor peak hours and avoid them
- Use multiple server endpoints if available

### 4. Error Handling
- Implement retry logic
- Handle rate limiting gracefully
- Monitor for account suspensions

## Troubleshooting

### Common Issues
1. **All accounts failing**: Check server status, API changes
2. **Low success rate**: Adjust timing, check account health
3. **Detection warnings**: Increase delays, rotate accounts more

### Debug Mode
```java
// Enable detailed logging
autobuy.setDebugMode(true);
// Check account status
autobuy.getAccountStatus();
// Monitor queue health
autobuy.getQueueHealth();
```

## Security Considerations

- Store account credentials securely
- Use environment variables for sensitive data
- Implement rate limiting
- Monitor for suspicious activity
- Regular account rotation

## Legal Disclaimer

This system is for educational purposes only. Users are responsible for complying with server terms of service and applicable laws. The developers are not responsible for any consequences of using this software.