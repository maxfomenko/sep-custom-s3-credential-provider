# sep-custom-s3-credential-provider

A sample implementation of a custom S3 Credential Provider for **Starburst Enterprise (479 LTS)**, enabling per-bucket credential mapping via environment variables.

---

## Prerequisites

- Java version matching the one used by your Starburst Enterprise deployment.
  For 479 LTS, **Java 25** is required.
- Maven

---

## Build

```bash
mvn package
```

After a successful build, the JAR will be available at:

```
target/starburst-dell-ecs-credentials-1.0.0.jar
```

---

## Deployment

Copy the JAR to the Hive plugin directory on **each Starburst Enterprise node** (coordinator and workers). Below is an example `initFile` script for Kubernetes-based deployments:

```bash
#!/bin/bash

# Install Custom S3 Credential Provider
cp /downloads/starburst-dell-ecs-credentials-1.0.0.jar /usr/lib/starburst/plugin/hive/
cp /downloads/starburst-dell-ecs-credentials-1.0.0.jar /usr/lib/starburst/plugin/iceberg/
cp /downloads/starburst-dell-ecs-credentials-1.0.0.jar /usr/lib/starburst/plugin/delta-lake/
cp /downloads/starburst-dell-ecs-credentials-1.0.0.jar /usr/lib/starburst/plugin/great-lakes/

exec /usr/lib/starburst/bin/run-starburst
```

---

## Configuration

### 1. Environment Variables

Set the following environment variables with the credentials each catalog should use to access S3:

| Variable | Description |
|---|---|
| `DELL_ECS_ACCESS_KEY_BUCKET_A` | Access key for Bucket A |
| `DELL_ECS_SECRET_KEY_BUCKET_A` | Secret key for Bucket A |
| `DELL_ECS_ACCESS_KEY_BUCKET_B` | Access key for Bucket B |
| `DELL_ECS_SECRET_KEY_BUCKET_B` | Secret key for Bucket B |

### 2. S3 Security Mapping

Create the security mapping JSON file (e.g. `sep-s3-security-mapping.json`) referencing the appropriate credential provider class for each S3 path prefix:

```json
{
  "mappings": [
    {
      "prefix": "s3://sb-max-fomenko/DataLake/starburst_data_catalog/s3_security_mapping_schema_1/",
      "customCredentialProviderClass": "com.starburst.custom.DellECSAwsCredentialsProviders$DellECSAwsCredentialsProviderBucketA"
    },
    {
      "prefix": "s3://sb-max-fomenko/DataLake/starburst_data_catalog/s3_security_mapping_schema_2/",
      "customCredentialProviderClass": "com.starburst.custom.DellECSAwsCredentialsProviders$DellECSAwsCredentialsProviderBucketB"
    },
    {
      "prefix": "s3://sb-max-fomenko/DataLake/",
      "customCredentialProviderClass": "com.starburst.custom.DellECSAwsCredentialsProviders$DellECSAwsCredentialsProviderBucketA"
    }
  ]
}
```

### 3. Kubernetes Secret

Store the mapping file as a Kubernetes secret:

```bash
kubectl create secret generic sep-s3-security-mapping \
  --from-file=sep-s3-security-mapping.json \
  -n mf-test
```

### 4. Mount the Secret

Add the secret as a volume in your Starburst Helm values:

```yaml
additionalVolumes:
  - path: /tmp/sep-s3-security-mapping
    volume:
      secret:
        secretName: "sep-s3-security-mapping"
```

### 5. Hive Catalog Configuration

Enable security mapping in your Hive catalog configuration:

```properties
s3.security-mapping.enabled=true
s3.security-mapping.config-file=/tmp/sep-s3-security-mapping/sep-s3-security-mapping.json
```