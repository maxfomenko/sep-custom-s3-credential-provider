package com.starburst.custom;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.util.Map;

public class DellECSAwsCredentialsProviders
{
    // Read once from environment at class load time
    private static final String DELL_ECS_ACCESS_KEY_BUCKET_A =
            System.getenv("DELL_ECS_ACCESS_KEY_BUCKET_A");
    private static final String DELL_ECS_SECRET_KEY_BUCKET_A =
            System.getenv("DELL_ECS_SECRET_KEY_BUCKET_A");

    private static final String DELL_ECS_ACCESS_KEY_BUCKET_B =
            System.getenv("DELL_ECS_ACCESS_KEY_BUCKET_B");
    private static final String DELL_ECS_SECRET_KEY_BUCKET_B =
            System.getenv("DELL_ECS_SECRET_KEY_BUCKET_B");

    public static class DellECSAwsCredentialsProviderBucketA
            implements AwsCredentialsProvider
    {
        public DellECSAwsCredentialsProviderBucketA(Map<String, String> properties)
        {
            // Optionally inspect properties if needed
        }

        @Override
        public AwsCredentials resolveCredentials()
        {
            if (DELL_ECS_ACCESS_KEY_BUCKET_A == null || DELL_ECS_SECRET_KEY_BUCKET_A == null) {
                throw new IllegalStateException(
                        "Environment variables DELL_ECS_ACCESS_KEY_BUCKET_A and " +
                        "DELL_ECS_SECRET_KEY_BUCKET_A must be set");
            }
            return AwsBasicCredentials.create(
                    DELL_ECS_ACCESS_KEY_BUCKET_A,
                    DELL_ECS_SECRET_KEY_BUCKET_A);
        }
    }

    public static class DellECSAwsCredentialsProviderBucketB
            implements AwsCredentialsProvider
    {
        public DellECSAwsCredentialsProviderBucketB(Map<String, String> properties)
        {
            // Optionally inspect properties if needed
        }

        @Override
        public AwsCredentials resolveCredentials()
        {
            if (DELL_ECS_ACCESS_KEY_BUCKET_B == null || DELL_ECS_SECRET_KEY_BUCKET_B == null) {
                throw new IllegalStateException(
                        "Environment variables DELL_ECS_ACCESS_KEY_BUCKET_B and " +
                        "DELL_ECS_SECRET_KEY_BUCKET_B must be set");
            }
            return AwsBasicCredentials.create(
                    DELL_ECS_ACCESS_KEY_BUCKET_B,
                    DELL_ECS_SECRET_KEY_BUCKET_B);
        }
    }
}