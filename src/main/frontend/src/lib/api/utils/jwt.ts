export function decodeJWT(token: string): any | null {
    const parts = token.split('.');
    if (parts.length !== 3) {
        return null; // Invalid JWT format
    }

    const encodedPayload = parts[1];
    const decodedPayload = Buffer.from(encodedPayload, 'base64').toString('utf-8');

    try {
        return JSON.parse(decodedPayload);
    } catch (error) {
        console.error('Error decoding JWT payload:', error);
        return null;
    }
}
