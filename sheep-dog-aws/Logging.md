# Logging Guide for sheep-dog-aws Service

This document provides comprehensive information about logging in the sheep-dog-aws Spring Boot service, particularly in a Kubernetes environment.

## Table of Contents

1. [Logging Configuration](#logging-configuration)
2. [Enabling/Disabling Debug Logging](#enablingdisabling-debug-logging)
3. [Viewing Logs with kubectl](#viewing-logs-with-kubectl)
4. [Persistent Logging](#persistent-logging)
5. [Saving Logs Before Container Termination](#saving-logs-before-container-termination)

## Logging Configuration

The sheep-dog-aws service uses Logback (SLF4J implementation) for logging. The configuration is defined in `src/main/resources/logback.xml`.

### Key Features

- Log level is controlled via the `LOG_LEVEL` environment variable
- Default log level is INFO if the environment variable is not set
- All logs (DEBUG, INFO, WARN, ERROR) are output to both console and persistent file storage
- Spring Framework and Hibernate logs are kept at INFO level by default
- Logs are stored on a persistent volume to survive pod restarts and terminations

### Log Format

Logs are formatted as follows:

```
yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message
```

Example:
```
2025-06-12 12:00:00.123 [http-nio-8080-exec-1] INFO  o.f.greeting.controller.GreetingController - Received greeting request for name: World
```

## Enabling/Disabling Debug Logging

### In Development Environment

When running the application locally, you can set the log level using:

```bash
# Windows
$env:LOG_LEVEL = "DEBUG"
java -jar sheep-dog-aws.jar

# Linux/Mac
LOG_LEVEL=DEBUG java -jar sheep-dog-aws.jar
```

### In Kubernetes Environment

#### Option 1: Update Deployment YAML

1. Modify the `LOG_LEVEL` environment variable in `kubernetes/base/deployment.yaml`:

```yaml
env:
  - name: LOG_LEVEL
    value: "DEBUG"  # Change from INFO to DEBUG
```

2. Apply the updated deployment:

```bash
kubectl apply -k kubernetes/overlays/dev
```

#### Option 2: Update Running Deployment

To change the log level without modifying the YAML file:

```bash
kubectl set env -n dev deployment/sheep-dog-aws LOG_LEVEL=DEBUG
```

To revert back to INFO level:

```bash
kubectl set env -n dev deployment/sheep-dog-aws LOG_LEVEL=INFO
```

#### Option 3: Using ConfigMap

For more flexibility, you can use a ConfigMap to manage logging configuration:

1. Create a ConfigMap:

```bash
kubectl create configmap sheep-dog-aws-config --from-literal=LOG_LEVEL=DEBUG
```

2. Update the deployment to use the ConfigMap:

```bash
kubectl set env -n dev deployment/sheep-dog-aws --from=configmap/sheep-dog-aws-config
```

## Viewing Logs with kubectl

### Basic Log Viewing

To view logs for the sheep-dog-aws pods:

```bash
kubectl logs -n dev -l app=sheep-dog-aws
```

This will show logs from one of the pods. To see logs from a specific pod:

```bash
kubectl logs -n dev <pod-name>
```

### Tailing Logs in PowerShell

To continuously stream logs (similar to `tail -f`):

```powershell
kubectl logs -f -l -n dev app=sheep-dog-aws
```

For a specific pod:

```powershell
kubectl logs -f -n dev <pod-name>
```

### Viewing Logs from Multiple Pods

To view logs from all pods in the deployment:

```powershell
kubectl logs -f -l -n dev app=sheep-dog-aws --all-containers=true
```

### Filtering Logs

To filter logs for specific content:

```powershell
kubectl logs -l -n dev app=sheep-dog-aws | Select-String "ERROR"
```

This PowerShell command is equivalent to `grep` in Linux and will show only lines containing "ERROR".

### Viewing Recent Logs

To see only the most recent logs:

```powershell
kubectl logs -l -n dev app=sheep-dog-aws --tail=100
```

This shows the last 100 log lines.

### PowerShell Function for Log Viewing

You can create a PowerShell function for easier log viewing:

```powershell
function Get-K8sLogs {
    param (
        [string]$app = "sheep-dog-aws",
        [int]$tail = 0,
        [switch]$follow,
        [string]$grep
    )
    
    $cmd = "kubectl logs -l -n dev app=$app"
    if ($tail -gt 0) { $cmd += " --tail=$tail" }
    if ($follow) { $cmd += " -f" }
    
    if ($grep) {
        Invoke-Expression $cmd | Select-String $grep
    } else {
        Invoke-Expression $cmd
    }
}
```

Usage examples:

```powershell
# Tail logs continuously
Get-K8sLogs -follow

# Show last 50 lines containing "ERROR"
Get-K8sLogs -tail 50 -grep "ERROR"
```

## Persistent Logging

The sheep-dog-aws service is configured to write logs to both the console (for kubectl logs) and to persistent storage. This ensures that logs are preserved even if a pod is terminated.

### Log Storage Configuration

Logs are stored in the following locations:

- **Console Output**: Standard output captured by kubectl
- **Persistent Volume**: `/logs/sheep-dog-aws.log` and daily rolled files

### Accessing Persistent Logs

To access the logs stored on the persistent volume:

1. Identify the pod name:
```bash
kubectl get pods -n dev -l app=sheep-dog-aws
```

2. Execute a command in the pod to view the logs:
```bash
kubectl exec -it -n dev <pod-name> -- cat /logs/sheep-dog-aws.log
```

Or to tail the logs:
```bash
kubectl exec -it -n dev <pod-name> -- tail -f /logs/sheep-dog-aws.log
```

### Log Rotation

Logs are automatically rotated daily and compressed to save space. The rotation policy is:

- Daily rotation with compression (gzip)
- 1 days retention period
- 1GB total size cap

## Saving Logs Before Container Termination

When a Kubernetes pod is terminated, its logs are preserved in two ways:

1. **Persistent Volume**: All logs are written to the persistent volume in real-time, ensuring they survive pod termination
2. **Termination Grace Period**: The pod has a termination grace period that allows time for logs to be flushed to storage

### Accessing Logs from Terminated Pods

To access logs from a terminated pod:

1. If the persistent volume is still mounted to another pod, you can access the logs through that pod
2. If no pods are running, you can start a temporary pod to mount the PVC:

```bash
kubectl run log-viewer --rm -it --image=alpine --overrides='
{
  "spec": {
    "volumes": [
      {
        "name": "logs-volume",
        "persistentVolumeClaim": {
          "claimName": "sheep-dog-aws-logs-pvc"
        }
      }
    ],
    "containers": [
      {
        "name": "log-viewer",
        "image": "alpine",
        "command": [
          "sh"
        ],
        "stdin": true,
        "tty": true,
        "volumeMounts": [
          {
            "name": "logs-volume",
            "mountPath": "/logs"
          }
        ]
      }
    ]
  }
}'
```

Once in the temporary pod, you can view the logs:
```bash
ls -la /logs
cat /logs/sheep-dog-aws.log
```
