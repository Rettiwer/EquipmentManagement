<script lang="ts">
    let className: string;

    export let outlined: boolean = false;
    export let type: "button" | "reset" | "submit" | null | undefined = "button";
    export { className as class };
</script>

<button
        {type}
        on:click
        class="{className} btn btn-primary" class:btn-outline="{outlined}" class:btn-secondary={outlined}
        {...$$restProps}>
    <slot></slot>
</button>
