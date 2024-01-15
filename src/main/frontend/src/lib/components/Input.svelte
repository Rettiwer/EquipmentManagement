<script lang="ts">
    import { onMount } from 'svelte';

    let inputElement: HTMLInputElement;
    let className = '';

    export let label: string = "", placeholder: string = "", parentClass: string = "";
    export { className as class };

    onMount(() => {
        if (inputElement.hasAttribute("autofocus")) {
            inputElement.focus();
        }
    });
</script>


<div class="form-control w-full {parentClass}">
    <label class="label" for="">
        <span class="label-text">{ label } {@html $$restProps.required ? '<span class="text-primary">*</span>' : ''}</span>
    </label>
    <input
            bind:this={inputElement}
            on:input
            {...$$restProps}
            placeholder="{ placeholder }" class="input input-bordered w-full mb-4 { className }" />
</div>
