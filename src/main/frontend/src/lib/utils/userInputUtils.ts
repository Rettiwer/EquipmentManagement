export function debounce(func: Function, delay: number): Function {
    let timeoutId: ReturnType<typeof setTimeout>;

    return function(this: any, ...args: any[]) {
        const context = this;

        clearTimeout(timeoutId);

        timeoutId = setTimeout(() => {
            func.apply(context, args);
        }, delay);
    };
}