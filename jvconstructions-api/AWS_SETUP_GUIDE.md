# AWS Setup Guide for JV Constructions Project

This guide covers the complete AWS setup process for the JV Constructions application, including IAM user creation, S3 bucket setup, and permission configuration.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [AWS Account Setup](#aws-account-setup)
3. [S3 Bucket Creation](#s3-bucket-creation)
4. [IAM User Creation](#iam-user-creation)
5. [Custom IAM Policy Creation](#custom-iam-policy-creation)
6. [CloudFront CDN Setup](#cloudfront-cdn-setup)
7. [Application Configuration](#application-configuration)
8. [Testing the Setup](#testing-the-setup)
9. [Security Best Practices](#security-best-practices)
10. [Troubleshooting](#troubleshooting)

## Prerequisites

- AWS Account with administrative access
- Basic understanding of AWS services (IAM, S3, CloudFront)
- Access to AWS Management Console

## AWS Account Setup

### 1. Login to AWS Console

1. Go to [AWS Management Console](https://console.aws.amazon.com/)
2. Sign in with your AWS account credentials
3. Ensure you're in the correct region (recommended: `ap-south-1` for India)

### 2. Verify Account Information

- Note your AWS Account ID (visible in the top-right corner)
- Ensure you have billing alerts set up
- Verify your account has the necessary service limits

## S3 Bucket Creation

### 1. Create S3 Bucket

1. Navigate to **S3** service in AWS Console
2. Click **"Create bucket"**
3. Configure the bucket:

   **General Configuration:**
   - **Bucket name**: `jvconstructions-media` (must be globally unique)
   - **Region**: `Asia Pacific (Mumbai) ap-south-1`

   **Object Ownership:**
   - Select **"ACLs enabled"**
   - Select **"Bucket owner preferred"**

   **Block Public Access:**
   - Uncheck **"Block all public access"** (we'll use CloudFront for public access)
   - Check the acknowledgment box

   **Bucket Versioning:**
   - Select **"Enable"** (recommended for data protection)

   **Default encryption:**
   - Select **"Enable"**
   - Choose **"Amazon S3 managed keys (SSE-S3)"**

4. Click **"Create bucket"**

### 2. Configure Bucket for CloudFront

1. Go to your bucket → **Permissions** tab
2. Scroll to **"Bucket Policy"** and click **"Edit"**
3. Add this policy to allow CloudFront access:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowCloudFrontServicePrincipal",
            "Effect": "Allow",
            "Principal": {
                "Service": "cloudfront.amazonaws.com"
            },
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::jvconstructions-media/*",
            "Condition": {
                "StringEquals": {
                    "AWS:SourceArn": "arn:aws:cloudfront::YOUR_ACCOUNT_ID:distribution/YOUR_DISTRIBUTION_ID"
                }
            }
        }
    ]
}
```

**Note**: Replace `YOUR_ACCOUNT_ID` and `YOUR_DISTRIBUTION_ID` with actual values after creating CloudFront distribution.

## IAM User Creation

### 1. Create IAM User

1. Navigate to **IAM** service in AWS Console
2. Click **"Users"** in the left sidebar
3. Click **"Create user"**
4. Configure the user:

   **User details:**
   - **User name**: `jv-constructions-app`
   - **Select AWS credential type**: ✅ **"Access key - Programmatic access"**

5. Click **"Next"**

### 2. Set Permissions

1. Select **"Attach policies directly"**
2. We'll create a custom policy in the next section
3. Click **"Next"** → **"Next"** → **"Create user"**

### 3. Generate Access Keys

1. After user creation, click on the user name
2. Go to **"Security credentials"** tab
3. Scroll to **"Access keys"** section
4. Click **"Create access key"**
5. Select **"Command Line Interface (CLI)"**
6. Check the confirmation checkbox
7. Click **"Next"**
8. Add description: `JV Constructions API Access`
9. Click **"Create access key"**

**⚠️ IMPORTANT**: Copy and save both:
- **Access Key ID**: `AKIA...`
- **Secret Access Key**: `...` (only shown once!)

## Custom IAM Policy Creation

### 1. Create Custom Policy

1. In IAM Console, go to **"Policies"**
2. Click **"Create policy"**
3. Click **"JSON"** tab
4. Paste the following policy:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "S3BucketAccess",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:GetBucketLocation"
            ],
            "Resource": "arn:aws:s3:::jvconstructions-media"
        },
        {
            "Sid": "S3ObjectAccess",
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "s3:DeleteObject",
                "s3:PutObjectAcl"
            ],
            "Resource": "arn:aws:s3:::jvconstructions-media/*"
        }
    ]
}
```

5. Click **"Next"**
6. **Policy name**: `JVConstructionsS3Policy`
7. **Description**: `S3 access policy for JV Constructions application`
8. Click **"Create policy"**

### 2. Attach Policy to User

1. Go to **"Users"** → `jv-constructions-app`
2. Click **"Add permissions"** → **"Attach policies directly"**
3. Search for `JVConstructionsS3Policy`
4. Select the policy and click **"Next"**
5. Click **"Add permissions"**

## CloudFront CDN Setup

### 1. Create CloudFront Distribution

1. Navigate to **CloudFront** service
2. Click **"Create distribution"**
3. Configure the distribution:

   **Origin settings:**
   - **Origin domain**: Select your S3 bucket (`jvconstructions-media.s3.ap-south-1.amazonaws.com`)
   - **Origin path**: Leave empty
   - **Origin access**: Select **"Legacy access identities"**
   - **Origin access identity**: Create new OAI
   - **Comment**: `JV Constructions Media OAI`

   **Default cache behavior:**
   - **Viewer protocol policy**: `Redirect HTTP to HTTPS`
   - **Allowed HTTP methods**: `GET, HEAD, OPTIONS, PUT, POST, PATCH, DELETE`
   - **Cache policy**: `CachingDisabled` (for dynamic content)
   - **Origin request policy**: `CORS-S3Origin`

   **Distribution settings:**
   - **Price class**: `Use all edge locations`
   - **Alternate domain names**: Leave empty (or add custom domain)
   - **SSL certificate**: `Default CloudFront certificate`
   - **Default root object**: Leave empty

4. Click **"Create distribution"**
5. Wait for status to change to **"Deployed"** (5-15 minutes)

### 2. Update S3 Bucket Policy

1. Go back to your S3 bucket → **Permissions** → **Bucket Policy**
2. Replace the policy with:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowCloudFrontServicePrincipal",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::cloudfront:user/CloudFront Origin Access Identity YOUR_OAI_ID"
            },
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::jvconstructions-media/*"
        }
    ]
}
```

**Note**: Replace `YOUR_OAI_ID` with the actual OAI ID from CloudFront.

## Application Configuration

### 1. Update application.yml

Update your `src/main/resources/application.yml`:

```yaml
media:
  s3:
    region: ap-south-1
    bucket: jvconstructions-media
    cdnDomain: YOUR_CLOUDFRONT_DOMAIN.cloudfront.net
    presignExpirySeconds: 300
    accessKey: YOUR_ACCESS_KEY_ID
    secretKey: YOUR_SECRET_ACCESS_KEY
```

**Replace:**
- `YOUR_CLOUDFRONT_DOMAIN`: Your CloudFront distribution domain
- `YOUR_ACCESS_KEY_ID`: The access key from step 3 of IAM user creation
- `YOUR_SECRET_ACCESS_KEY`: The secret key from step 3 of IAM user creation

### 2. Environment Variables (Alternative)

For better security, use environment variables instead of hardcoded credentials:

```bash
export AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY_ID
export AWS_SECRET_ACCESS_KEY=YOUR_SECRET_ACCESS_KEY
export AWS_DEFAULT_REGION=ap-south-1
```

Then update `AwsS3Config.java` to use environment variables:

```java
@Value("${AWS_ACCESS_KEY_ID}")
String accessKey;

@Value("${AWS_SECRET_ACCESS_KEY}")
String secretKey;
```

## Testing the Setup

### 1. Test AWS Connectivity

```bash
curl http://localhost:8082/aws-test-public
```

Expected response:
```json
{
  "status": "success",
  "message": "AWS S3 connection test successful",
  "testUrl": "https://YOUR_CLOUDFRONT_DOMAIN.cloudfront.net/test/connection-test.txt",
  "bucket": "jvconstructions-media"
}
```

### 2. Test Image Upload

```bash
curl -X POST http://localhost:8082/api/v1/projects/1/images/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/test-image.jpg" \
  -F "isHero=true" \
  -F "sortOrder=1"
```

Expected response:
```json
{
  "id": 123,
  "url": "https://YOUR_CLOUDFRONT_DOMAIN.cloudfront.net/projects/villa-001/images/uuid-123.jpg",
  "mimeType": "image/jpeg",
  "width": null,
  "height": null,
  "sortOrder": 1,
  "hero": true
}
```

## Security Best Practices

### 1. Credential Management

- ✅ **Never commit credentials to version control**
- ✅ **Use environment variables for production**
- ✅ **Rotate access keys regularly (every 90 days)**
- ✅ **Use IAM roles instead of access keys when possible**

### 2. S3 Security

- ✅ **Enable bucket versioning**
- ✅ **Enable server-side encryption**
- ✅ **Use CloudFront for public access (not direct S3)**
- ✅ **Implement proper CORS policies**

### 3. IAM Security

- ✅ **Follow principle of least privilege**
- ✅ **Use custom policies instead of managed policies when possible**
- ✅ **Regularly audit IAM permissions**
- ✅ **Enable CloudTrail for API logging**

### 4. Network Security

- ✅ **Use HTTPS for all communications**
- ✅ **Implement proper CORS headers**
- ✅ **Use CloudFront for DDoS protection**

## Troubleshooting

### Common Issues

#### 1. Access Denied (403) Error

**Error**: `User is not authorized to perform: s3:PutObject`

**Solution**:
- Verify IAM policy is attached to the user
- Check if policy allows the specific S3 action
- Ensure bucket name matches in policy

#### 2. Signature Does Not Match Error

**Error**: `The request signature we calculated does not match the signature you provided`

**Solution**:
- Verify access key and secret key are correct
- Check if credentials have expired
- Ensure region is correct

#### 3. Bucket Not Found Error

**Error**: `The specified bucket does not exist`

**Solution**:
- Verify bucket name in application.yml
- Check if bucket exists in the correct region
- Ensure bucket name is globally unique

#### 4. CloudFront 403 Error

**Error**: `Access Denied` when accessing images via CloudFront

**Solution**:
- Update S3 bucket policy with correct OAI ID
- Verify CloudFront distribution is deployed
- Check if origin access identity is properly configured

### Debug Commands

#### Test AWS CLI Access
```bash
aws s3 ls s3://jvconstructions-media/
```

#### Test S3 Upload
```bash
aws s3 cp test-file.txt s3://jvconstructions-media/test/
```

#### Check IAM Permissions
```bash
aws iam list-attached-user-policies --user-name jv-constructions-app
```

## Cost Optimization

### 1. S3 Storage Classes

- **Standard**: For frequently accessed images
- **IA (Infrequent Access)**: For older project images
- **Glacier**: For archived images

### 2. CloudFront Caching

- Set appropriate cache TTL for different content types
- Use cache invalidation for updated images
- Monitor CloudFront usage and costs

### 3. Monitoring

- Set up billing alerts
- Monitor S3 and CloudFront usage
- Use AWS Cost Explorer for analysis

## Support

For additional help:

1. **AWS Documentation**: [S3 Developer Guide](https://docs.aws.amazon.com/s3/)
2. **AWS Support**: Available through AWS Console
3. **Community Forums**: AWS re:Post, Stack Overflow

---

**Last Updated**: September 2024
**Version**: 1.0
**Author**: JV Constructions Development Team
