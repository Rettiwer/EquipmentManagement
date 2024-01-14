<script lang="ts">
    export let title: string, selected: boolean = false;
</script>

<button class="btn btn-ghost btn-block text-white flex justify-start" class:btn-active="{selected}">
    <slot name="before"></slot>
    <span class="ml-3 flex-1 text-left whitespace-nowrap truncate">{title}</span>
    <slot name="after"></slot>
</button>

