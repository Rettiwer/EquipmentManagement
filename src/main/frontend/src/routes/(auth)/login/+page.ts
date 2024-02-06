export const prerender = false;

export function load({ params }) {
    return {
        post: {
            title: `Title for  goes here`,
            content: `Content for  goes here`
        }
    };
}