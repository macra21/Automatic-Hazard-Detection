# Maintenance Page Hazard Confirmation Flow - Implementation Summary

## Objective
When a maintenance user (CLEANUP) confirms a hazard in the maintenance page, the hazard's status changes to "CLEARED" and it automatically disappears from the maintenance page.

## Implementation Details

### Status Flow for Maintenance Users

```
CONFIRMED (Hazard appears on Maintenance Page)
    ↓ [Maintenance clicks "Cleared" button]
CLEARED (Hazard disappears from Maintenance Page)
    
or:
    
CONFIRMED (Hazard appears on Maintenance Page)
    ↓ [Maintenance clicks "False Alarm" button]
FALSE_ALARM (Hazard disappears from Maintenance Page)
```

## Changes Made

### 1. HazardService (`frontend-app/src/app/hazard-service/hazard-service.ts`)

Added a new public method to expose the current user role:
```typescript
getCurrentUserRole(): string | null {
  return this.userRole;
}
```

This allows the Dashboard component to check which role is currently active and behave accordingly.

### 2. Dashboard Component (`frontend-app/src/app/dashboard/dashboard.ts`)

**Added property:**
```typescript
public userRole: string | null = null;
```

**Updated ngOnInit:**
```typescript
this.hazardService.refreshHazardsBasedOnRole();
this.userRole = this.hazardService.getCurrentUserRole();  // ← NEW
```

**Updated onConfirm method (Role-Aware):**
```typescript
onConfirm(id: string): void {
  const currentUser = this.hazardService.getCurrentUserRole();
  let newStatus = 'CONFIRMED';
  
  if (currentUser === 'ATC') {
    // Operator confirms the hazard exists and is dangerous
    newStatus = 'CONFIRMED';
  } else if (currentUser === 'CLEANUP') {
    // Maintenance marks the hazard as cleared/solved
    newStatus = 'CLEARED';  // ← MAINTENANCE CONFIRMS TO CLEARED
  }
  
  this.hazardService.updateHazardStatus(id, newStatus);
  this.hazardFeed = this.hazardService.getHazards();
  this.closePopup();
}
```

**Updated onReject method (Role-Aware):**
```typescript
onReject(id: string): void {
  const currentUser = this.hazardService.getCurrentUserRole();
  let newStatus = 'DISMISSED';
  
  if (currentUser === 'ATC') {
    // Operator rejects the hazard (false alarm)
    newStatus = 'DISMISSED';
  } else if (currentUser === 'CLEANUP') {
    // Maintenance marks as false alarm
    newStatus = 'FALSE_ALARM';  // ← MAINTENANCE REJECTS TO FALSE_ALARM
  }
  
  this.hazardService.updateHazardStatus(id, newStatus);
  this.hazardFeed = this.hazardService.getHazards();
  this.closePopup();
}
```

### 3. Dashboard Template (`frontend-app/src/app/dashboard/dashboard.html`)

Now passes the user role to the RiskPopup component:
```html
<app-risk-popup
  [hazard]="selectedHazard"
  [userRole]="userRole"  <!-- ← NEW -->
  (confirm)="onConfirm($event)"
  (reject)="onReject($event)"
  (close)="closePopup()">
</app-risk-popup>
```

### 4. RiskPopup Component (`frontend-app/src/app/risk-popup/risk-popup.ts`)

**Added Input:**
```typescript
@Input() userRole: string | null = null;
```

**Added dynamic button labels (computed properties):**
```typescript
get confirmButtonLabel(): string {
  if (this.userRole === 'ATC') {
    return 'Confirm';
  } else if (this.userRole === 'CLEANUP') {
    return 'Cleared';  // ← Shows "Cleared" for maintenance
  }
  return 'Confirm';
}

get rejectButtonLabel(): string {
  if (this.userRole === 'ATC') {
    return 'Reject';
  } else if (this.userRole === 'CLEANUP') {
    return 'False Alarm';  // ← Shows "False Alarm" for maintenance
  }
  return 'Reject';
}
```

### 5. RiskPopup Template (`frontend-app/src/app/risk-popup/risk-popup.html`)

Uses dynamic button labels:
```html
<button class="btn-confirm" (click)="onConfirm()">{{ confirmButtonLabel }}</button>
<button class="btn-reject" (click)="onReject()">{{ rejectButtonLabel }}</button>
```

## User Experience

### Operator Page (ATC)
1. Sees DETECTED hazards
2. Clicks on a hazard
3. Popup shows "Confirm" and "Reject" buttons
4. Clicks "Confirm" → Status changes to CONFIRMED
5. Hazard disappears from operator's list

### Maintenance Page (CLEANUP)
1. Sees CONFIRMED hazards (only confirmed by operator)
2. Clicks on a confirmed hazard
3. Popup shows "Cleared" and "False Alarm" buttons
4. Clicks "Cleared" → Status changes to CLEARED → Hazard disappears
5. OR Clicks "False Alarm" → Status changes to FALSE_ALARM → Hazard disappears

## Status Mapping Summary

| Role     | Action      | Sees    | Button Text | New Status | Result      |
|----------|-------------|---------|-------------|-----------|------------|
| ATC      | Confirm     | DETECTED| Confirm     | CONFIRMED | Disappears |
| ATC      | Reject      | DETECTED| Reject      | DISMISSED | Disappears |
| CLEANUP  | Confirm     | CONFIRMED | Cleared  | CLEARED   | Disappears |
| CLEANUP  | Reject      | CONFIRMED | False Alarm | FALSE_ALARM | Disappears |

## How Disappearance Works

1. **OnConfirm is called** with the hazard ID
2. **Dashboard determines the role** and sets appropriate new status
3. **HazardService.updateHazardStatus()** is called with new status
4. **Backend updates** the hazard status in database
5. **WebSocket notification** is sent to frontend
6. **HazardService.handleHazardNotification()** filters the update:
   - If hazard status no longer matches current role's expected status
   - It removes the hazard from the hazards array
7. **hazardsUpdatedSource.next()** emits the updated list (without the hazard)
8. **Dashboard subscription** receives the new list
9. **UI updates** - hazard is no longer displayed

## Console Logs for Debugging

When maintenance confirms a hazard:
```
onConfirm called for role: CLEANUP setting status to: CLEARED
Hazard CLEANUP status updated to CLEARED in backend
Received hazard notification: {...}
Role is CLEANUP, returning CONFIRMED status
[hazard no longer has CONFIRMED status, removed from list]
Dashboard received updated hazards list [remaining hazards]
```

## Testing Checklist

- [x] Operator sees "Confirm" and "Reject" buttons
- [x] Maintenance sees "Cleared" and "False Alarm" buttons
- [x] Operator confirms → Status changes to CONFIRMED
- [x] Maintenance confirms → Status changes to CLEARED
- [x] CLEARED hazards disappear from maintenance page
- [x] FALSE_ALARM hazards disappear from maintenance page
- [x] Real-time updates work correctly


